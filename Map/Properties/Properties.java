package java.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import jdk.internal.misc.JavaObjectInputStreamAccess;
import jdk.internal.misc.SharedSecrets;
import jdk.internal.util.xml.PropertiesDefaultHandler;

public class Properties  extends Hashtable<Object, Object>
{
  private static final long serialVersionUID = 4112578634029874840L;
  protected Properties defaults;
  private transient ConcurrentHashMap<Object, Object> map;
  
  public Properties()
  {
    this(null, 8);
  }
  
  public Properties(int initialCapacity)
  {
    this(null, initialCapacity);
  }
  
  public Properties(Properties defaults)
  {
    this(defaults, 8);
  }
  
  private Properties(Properties defaults, int initialCapacity)
  {
    super((Void)null);
    this.map = new ConcurrentHashMap(initialCapacity);
    this.defaults = defaults;
  }
  
  public synchronized Object setProperty(String key, String value)
  {
    return put(key, value);
  }
  
  public synchronized void load(Reader reader)
    throws IOException
  {
    Objects.requireNonNull(reader, "reader parameter is null");
    load0(new LineReader(reader));
  }
  
  public synchronized void load(InputStream inStream)
    throws IOException
  {
    Objects.requireNonNull(inStream, "inStream parameter is null");
    load0(new LineReader(inStream));
  }
  
  private void load0(LineReader lr)
    throws IOException
  {
    char[] convtBuf = new char[1024];
    int limit;
    while ((limit = lr.readLine()) >= 0)
    {
      char c = '\000';
      int keyLen = 0;
      int valueStart = limit;
      boolean hasSep = false;
      

      boolean precedingBackslash = false;
      while (keyLen < limit)
      {
        c = lr.lineBuf[keyLen];
        if (((c == '=') || (c == ':')) && (!precedingBackslash))
        {
          valueStart = keyLen + 1;
          hasSep = true;
          break;
        }
        if (((c == ' ') || (c == '\t') || (c == '\f')) && (!precedingBackslash))
        {
          valueStart = keyLen + 1;
          break;
        }
        if (c == '\\') {
          precedingBackslash = !precedingBackslash;
        } else {
          precedingBackslash = false;
        }
        keyLen++;
      }
      while (valueStart < limit)
      {
        c = lr.lineBuf[valueStart];
        if ((c != ' ') && (c != '\t') && (c != '\f'))
        {
          if ((hasSep) || ((c != '=') && (c != ':'))) {
            break;
          }
          hasSep = true;
        }
        valueStart++;
      }
      String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
      String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
      put(key, value);
    }
  }
  
  class LineReader
  {
    byte[] inByteBuf;
    char[] inCharBuf;
    
    public LineReader(InputStream inStream)
    {
      this.inStream = inStream;
      this.inByteBuf = new byte[8192];
    }
    
    public LineReader(Reader reader)
    {
      this.reader = reader;
      this.inCharBuf = new char[8192];
    }
    
    char[] lineBuf = new char[1024];
    int inLimit = 0;
    int inOff = 0;
    InputStream inStream;
    Reader reader;
    
    int readLine()
      throws IOException
    {
      int len = 0;
      char c = '\000';
      
      boolean skipWhiteSpace = true;
      boolean isCommentLine = false;
      boolean isNewLine = true;
      boolean appendedLineBegin = false;
      boolean precedingBackslash = false;
      boolean skipLF = false;
      for (;;)
      {
        if (this.inOff >= this.inLimit)
        {
          this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf));
          this.inOff = 0;
          if (this.inLimit <= 0)
          {
            if ((len == 0) || (isCommentLine)) {
              return -1;
            }
            if (precedingBackslash) {
              len--;
            }
            return len;
          }
        }
        if (this.inStream != null) {
          c = (char)(this.inByteBuf[(this.inOff++)] & 0xFF);
        } else {
          c = this.inCharBuf[(this.inOff++)];
        }
        if (skipLF)
        {
          skipLF = false;
          if (c == '\n') {}
        }
        else if (skipWhiteSpace)
        {
          if ((c != ' ') && (c != '\t') && (c != '\f') && (
          

            (appendedLineBegin) || ((c != '\r') && (c != '\n'))))
          {
            skipWhiteSpace = false;
            appendedLineBegin = false;
          }
        }
        else
        {
          if (isNewLine)
          {
            isNewLine = false;
            if ((c == '#') || (c == '!'))
            {
              if (this.inStream != null) {
                while (this.inOff < this.inLimit)
                {
                  byte b = this.inByteBuf[(this.inOff++)];
                  if ((b == 10) || (b == 13) || (b == 92))
                  {
                    c = (char)(b & 0xFF);
                    break;
                  }
                }
              }
              while (this.inOff < this.inLimit)
              {
                c = this.inCharBuf[(this.inOff++)];
                if ((c != '\n') && (c != '\r')) {
                  if (c == '\\') {
                    break;
                  }
                }
              }
              isCommentLine = true;
            }
          }
          if ((c != '\n') && (c != '\r'))
          {
            this.lineBuf[(len++)] = c;
            if (len == this.lineBuf.length)
            {
              int newLength = this.lineBuf.length * 2;
              if (newLength < 0) {
                newLength = 2147483647;
              }
              char[] buf = new char[newLength];
              System.arraycopy(this.lineBuf, 0, buf, 0, this.lineBuf.length);
              this.lineBuf = buf;
            }
            if (c == '\\') {
              precedingBackslash = !precedingBackslash;
            } else {
              precedingBackslash = false;
            }
          }
          else if ((isCommentLine) || (len == 0))
          {
            isCommentLine = false;
            isNewLine = true;
            skipWhiteSpace = true;
            len = 0;
          }
          else
          {
            if (this.inOff >= this.inLimit)
            {
              this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf));
              this.inOff = 0;
              if (this.inLimit <= 0)
              {
                if (precedingBackslash) {
                  len--;
                }
                return len;
              }
            }
            if (!precedingBackslash) {
              break;
            }
            len--;
            
            skipWhiteSpace = true;
            appendedLineBegin = true;
            precedingBackslash = false;
            if (c == '\r') {
              skipLF = true;
            }
          }
        }
      }
      return len;
    }
  }
  
  private String loadConvert(char[] in, int off, int len, char[] convtBuf)
  {
    if (convtBuf.length < len)
    {
      int newLen = len * 2;
      if (newLen < 0) {
        newLen = 2147483647;
      }
      convtBuf = new char[newLen];
    }
    char[] out = convtBuf;
    int outLen = 0;
    int end = off + len;
    while (off < end)
    {
      char aChar = in[(off++)];
      if (aChar == '\\')
      {
        aChar = in[(off++)];
        if (aChar == 'u')
        {
          int value = 0;
          for (int i = 0; i < 4; i++)
          {
            aChar = in[(off++)];
            switch (aChar)
            {
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': 
              value = (value << 4) + aChar - 48;
              break;
            case 'a': 
            case 'b': 
            case 'c': 
            case 'd': 
            case 'e': 
            case 'f': 
              value = (value << 4) + 10 + aChar - 97;
              break;
            case 'A': 
            case 'B': 
            case 'C': 
            case 'D': 
            case 'E': 
            case 'F': 
              value = (value << 4) + 10 + aChar - 65;
              break;
            case ':': 
            case ';': 
            case '<': 
            case '=': 
            case '>': 
            case '?': 
            case '@': 
            case 'G': 
            case 'H': 
            case 'I': 
            case 'J': 
            case 'K': 
            case 'L': 
            case 'M': 
            case 'N': 
            case 'O': 
            case 'P': 
            case 'Q': 
            case 'R': 
            case 'S': 
            case 'T': 
            case 'U': 
            case 'V': 
            case 'W': 
            case 'X': 
            case 'Y': 
            case 'Z': 
            case '[': 
            case '\\': 
            case ']': 
            case '^': 
            case '_': 
            case '`': 
            default: 
              throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
          }
          out[(outLen++)] = ((char)value);
        }
        else
        {
          if (aChar == 't') {
            aChar = '\t';
          } else if (aChar == 'r') {
            aChar = '\r';
          } else if (aChar == 'n') {
            aChar = '\n';
          } else if (aChar == 'f') {
            aChar = '\f';
          }
          out[(outLen++)] = aChar;
        }
      }
      else
      {
        out[(outLen++)] = aChar;
      }
    }
    return new String(out, 0, outLen);
  }
  
  private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode)
  {
    int len = theString.length();
    int bufLen = len * 2;
    if (bufLen < 0) {
      bufLen = 2147483647;
    }
    StringBuilder outBuffer = new StringBuilder(bufLen);
    for (int x = 0; x < len; x++)
    {
      char aChar = theString.charAt(x);
      if ((aChar > '=') && (aChar < ''))
      {
        if (aChar == '\\')
        {
          outBuffer.append('\\');outBuffer.append('\\');
        }
        else
        {
          outBuffer.append(aChar);
        }
      }
      else {
        switch (aChar)
        {
        case ' ': 
          if ((x == 0) || (escapeSpace)) {
            outBuffer.append('\\');
          }
          outBuffer.append(' ');
          break;
        case '\t': 
          outBuffer.append('\\');outBuffer.append('t');
          break;
        case '\n': 
          outBuffer.append('\\');outBuffer.append('n');
          break;
        case '\r': 
          outBuffer.append('\\');outBuffer.append('r');
          break;
        case '\f': 
          outBuffer.append('\\');outBuffer.append('f');
          break;
        case '!': 
        case '#': 
        case ':': 
        case '=': 
          outBuffer.append('\\');outBuffer.append(aChar);
          break;
        default: 
          if ((((aChar < ' ') || (aChar > '~')) & escapeUnicode))
          {
            outBuffer.append('\\');
            outBuffer.append('u');
            outBuffer.append(toHex(aChar >> '\f' & 0xF));
            outBuffer.append(toHex(aChar >> '\b' & 0xF));
            outBuffer.append(toHex(aChar >> '\004' & 0xF));
            outBuffer.append(toHex(aChar & 0xF));
          }
          else
          {
            outBuffer.append(aChar);
          }
          break;
        }
      }
    }
    return outBuffer.toString();
  }
  
  private static void writeComments(BufferedWriter bw, String comments)
    throws IOException
  {
    bw.write("#");
    int len = comments.length();
    int current = 0;
    int last = 0;
    char[] uu = new char[6];
    uu[0] = '\\';
    uu[1] = 'u';
    while (current < len)
    {
      char c = comments.charAt(current);
      if ((c > 'ÿ') || (c == '\n') || (c == '\r'))
      {
        if (last != current) {
          bw.write(comments.substring(last, current));
        }
        if (c > 'ÿ')
        {
          uu[2] = toHex(c >> '\f' & 0xF);
          uu[3] = toHex(c >> '\b' & 0xF);
          uu[4] = toHex(c >> '\004' & 0xF);
          uu[5] = toHex(c & 0xF);
          bw.write(new String(uu));
        }
        else
        {
          bw.newLine();
          if ((c == '\r') && (current != len - 1)) {
            if (comments.charAt(current + 1) == '\n') {
              current++;
            }
          }
          if ((current == len - 1) || (
            (comments.charAt(current + 1) != '#') && 
            (comments.charAt(current + 1) != '!'))) {
            bw.write("#");
          }
        }
        last = current + 1;
      }
      current++;
    }
    if (last != current) {
      bw.write(comments.substring(last, current));
    }
    bw.newLine();
  }
  
  @Deprecated
  public void save(OutputStream out, String comments)
  {
    try
    {
      store(out, comments);
    }
    catch (IOException localIOException) {}
  }
  
  public void store(Writer writer, String comments)
    throws IOException
  {
    store0((writer instanceof BufferedWriter) ? (BufferedWriter)writer : 
      new BufferedWriter(writer), comments, false);
  }
  
  public void store(OutputStream out, String comments)
    throws IOException
  {
    store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
  }
  
  private void store0(BufferedWriter bw, String comments, boolean escUnicode)
    throws IOException
  {
    if (comments != null) {
      writeComments(bw, comments);
    }
    bw.write("#" + new Date().toString());
    bw.newLine();
    synchronized (this)
    {
      for (Map.Entry<Object, Object> e : entrySet())
      {
        String key = (String)e.getKey();
        String val = (String)e.getValue();
        key = saveConvert(key, true, escUnicode);
        


        val = saveConvert(val, false, escUnicode);
        bw.write(key + "=" + val);
        bw.newLine();
      }
    }
    bw.flush();
  }
  
  public synchronized void loadFromXML(InputStream in)
    throws IOException, InvalidPropertiesFormatException
  {
    Objects.requireNonNull(in);
    PropertiesDefaultHandler handler = new PropertiesDefaultHandler();
    handler.load(this, in);
    in.close();
  }
  
  public void storeToXML(OutputStream os, String comment)
    throws IOException
  {
    storeToXML(os, comment, "UTF-8");
  }
  
  public void storeToXML(OutputStream os, String comment, String encoding)
    throws IOException
  {
    Objects.requireNonNull(os);
    Objects.requireNonNull(encoding);
    try
    {
      Charset charset = Charset.forName(encoding);
      storeToXML(os, comment, charset);
    }
    catch (IllegalCharsetNameException|UnsupportedCharsetException e)
    {
      throw new UnsupportedEncodingException(encoding);
    }
  }
  
  public void storeToXML(OutputStream os, String comment, Charset charset)
    throws IOException
  {
    Objects.requireNonNull(os, "OutputStream");
    Objects.requireNonNull(charset, "Charset");
    PropertiesDefaultHandler handler = new PropertiesDefaultHandler();
    handler.store(this, os, comment, charset);
  }
  
  public String getProperty(String key)
  {
    Object oval = this.map.get(key);
    String sval = (oval instanceof String) ? (String)oval : null;
    return (sval == null) && (this.defaults != null) ? this.defaults.getProperty(key) : sval;
  }
  
  public String getProperty(String key, String defaultValue)
  {
    String val = getProperty(key);
    return val == null ? defaultValue : val;
  }
  
  public Enumeration<?> propertyNames()
  {
    Hashtable<String, Object> h = new Hashtable();
    enumerate(h);
    return h.keys();
  }
  
  public Set<String> stringPropertyNames()
  {
    Map<String, String> h = new HashMap();
    enumerateStringProperties(h);
    return Collections.unmodifiableSet(h.keySet());
  }
  
  public void list(PrintStream out)
  {
    out.println("-- listing properties --");
    Map<String, Object> h = new HashMap();
    enumerate(h);
    for (Map.Entry<String, Object> e : h.entrySet())
    {
      String key = (String)e.getKey();
      String val = (String)e.getValue();
      if (val.length() > 40) {
        val = val.substring(0, 37) + "...";
      }
      out.println(key + "=" + val);
    }
  }
  
  public void list(PrintWriter out)
  {
    out.println("-- listing properties --");
    Map<String, Object> h = new HashMap();
    enumerate(h);
    for (Map.Entry<String, Object> e : h.entrySet())
    {
      String key = (String)e.getKey();
      String val = (String)e.getValue();
      if (val.length() > 40) {
        val = val.substring(0, 37) + "...";
      }
      out.println(key + "=" + val);
    }
  }
  
  private void enumerate(Map<String, Object> h)
  {
    if (this.defaults != null) {
      this.defaults.enumerate(h);
    }
    for (Map.Entry<Object, Object> e : entrySet())
    {
      String key = (String)e.getKey();
      h.put(key, e.getValue());
    }
  }
  
  private void enumerateStringProperties(Map<String, String> h)
  {
    if (this.defaults != null) {
      this.defaults.enumerateStringProperties(h);
    }
    for (Map.Entry<Object, Object> e : entrySet())
    {
      Object k = e.getKey();
      Object v = e.getValue();
      if (((k instanceof String)) && ((v instanceof String))) {
        h.put((String)k, (String)v);
      }
    }
  }
  
  private static char toHex(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }
  
  private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  
  public int size()
  {
    return this.map.size();
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public Enumeration<Object> keys()
  {
    return Collections.enumeration(this.map.keySet());
  }
  
  public Enumeration<Object> elements()
  {
    return Collections.enumeration(this.map.values());
  }
  
  public boolean contains(Object value)
  {
    return this.map.contains(value);
  }
  
  public boolean containsValue(Object value)
  {
    return this.map.containsValue(value);
  }
  
  public boolean containsKey(Object key)
  {
    return this.map.containsKey(key);
  }
  
  public Object get(Object key)
  {
    return this.map.get(key);
  }
  
  public synchronized Object put(Object key, Object value)
  {
    return this.map.put(key, value);
  }
  
  public synchronized Object remove(Object key)
  {
    return this.map.remove(key);
  }
  
  public synchronized void putAll(Map<?, ?> t)
  {
    this.map.putAll(t);
  }
  
  public synchronized void clear()
  {
    this.map.clear();
  }
  
  public synchronized String toString()
  {
    return this.map.toString();
  }
  
  public Set<Object> keySet()
  {
    return Collections.synchronizedSet(this.map.keySet(), this);
  }
  
  public Collection<Object> values()
  {
    return Collections.synchronizedCollection(this.map.values(), this);
  }
  
  public Set<Map.Entry<Object, Object>> entrySet()
  {
    return Collections.synchronizedSet(new EntrySet(this.map.entrySet(), null), this);
  }
  
  private static class EntrySet
    implements Set<Map.Entry<Object, Object>>
  {
    private Set<Map.Entry<Object, Object>> entrySet;
    
    private EntrySet(Set<Map.Entry<Object, Object>> entrySet)
    {
      this.entrySet = entrySet;
    }
    
    public int size()
    {
      return this.entrySet.size();
    }
    
    public boolean isEmpty()
    {
      return this.entrySet.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return this.entrySet.contains(o);
    }
    
    public Object[] toArray()
    {
      return this.entrySet.toArray();
    }
    
    public <T> T[] toArray(T[] a)
    {
      return this.entrySet.toArray(a);
    }
    
    public void clear()
    {
      this.entrySet.clear();
    }
    
    public boolean remove(Object o)
    {
      return this.entrySet.remove(o);
    }
    
    public boolean add(Map.Entry<Object, Object> e)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends Map.Entry<Object, Object>> c)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean containsAll(Collection<?> c)
    {
      return this.entrySet.containsAll(c);
    }
    
    public boolean removeAll(Collection<?> c)
    {
      return this.entrySet.removeAll(c);
    }
    
    public boolean retainAll(Collection<?> c)
    {
      return this.entrySet.retainAll(c);
    }
    
    public Iterator<Map.Entry<Object, Object>> iterator()
    {
      return this.entrySet.iterator();
    }
  }
  
  public synchronized boolean equals(Object o)
  {
    return this.map.equals(o);
  }
  
  public synchronized int hashCode()
  {
    return this.map.hashCode();
  }
  
  public Object getOrDefault(Object key, Object defaultValue)
  {
    return this.map.getOrDefault(key, defaultValue);
  }
  
  public synchronized void forEach(BiConsumer<? super Object, ? super Object> action)
  {
    this.map.forEach(action);
  }
  
  public synchronized void replaceAll(BiFunction<? super Object, ? super Object, ?> function)
  {
    this.map.replaceAll(function);
  }
  
  public synchronized Object putIfAbsent(Object key, Object value)
  {
    return this.map.putIfAbsent(key, value);
  }
  
  public synchronized boolean remove(Object key, Object value)
  {
    return this.map.remove(key, value);
  }
  
  public synchronized boolean replace(Object key, Object oldValue, Object newValue)
  {
    return this.map.replace(key, oldValue, newValue);
  }
  
  public synchronized Object replace(Object key, Object value)
  {
    return this.map.replace(key, value);
  }
  
  public synchronized Object computeIfAbsent(Object key, Function<? super Object, ?> mappingFunction)
  {
    return this.map.computeIfAbsent(key, mappingFunction);
  }
  
  public synchronized Object computeIfPresent(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction)
  {
    return this.map.computeIfPresent(key, remappingFunction);
  }
  
  public synchronized Object compute(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction)
  {
    return this.map.compute(key, remappingFunction);
  }
  
  public synchronized Object merge(Object key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction)
  {
    return this.map.merge(key, value, remappingFunction);
  }
  
  protected void rehash() {}
  
  public synchronized Object clone()
  {
    Properties clone = (Properties)cloneHashtable();
    clone.map = new ConcurrentHashMap(this.map);
    return clone;
  }
  
  void writeHashtable(ObjectOutputStream s)
    throws IOException
  {
    List<Object> entryStack = new ArrayList(this.map.size() * 2);
    for (Map.Entry<Object, Object> entry : this.map.entrySet())
    {
      entryStack.add(entry.getValue());
      entryStack.add(entry.getKey());
    }
    float loadFactor = 0.75F;
    int count = entryStack.size() / 2;
    int length = (int)(count / loadFactor) + count / 20 + 3;
    if ((length > count) && ((length & 0x1) == 0)) {
      length--;
    }
    synchronized (this.map)
    {
      defaultWriteHashtable(s, length, loadFactor);
    }
    s.writeInt(length);
    s.writeInt(count);
    for (int i = entryStack.size() - 1; i >= 0; i--) {
      s.writeObject(entryStack.get(i));
    }
  }
  
  void readHashtable(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    

    int origlength = s.readInt();
    int elements = s.readInt();
    if (elements < 0) {
      throw new StreamCorruptedException("Illegal # of Elements: " + elements);
    }
    SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, [Ljava.util.Map.Entry.class, HashMap.tableSizeFor((int)(elements / 0.75D)));
    

    this.map = new ConcurrentHashMap(elements);
    for (; elements > 0; elements--)
    {
      Object key = s.readObject();
      Object value = s.readObject();
      this.map.put(key, value);
    }
  }
}
