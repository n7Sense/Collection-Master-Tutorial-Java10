

                                                                        Collection(I)
                                                                               |
                                        +------------------------------------------------------------------------------------------+
                                        |                                   |                                                      |
                                    List(I)                              Queue(I) <--\                                           Set(I)
                                    /\                                 /    |         \                                          /      \
                                    |                                 /     |          \                                        /        \
                          ArrayList<|                                /      |           \                            HashSet <-/          \->SortedSet(I) 
                            |       |                               /       |            \                                |                   |
                            |       |>LinkedList --------->Dequeue(I)   PriorityQueue     BlockingQueue(I)                |>LinkedHashSet     |>NaviganleSet(I)
              AttributeList<|       |                         |     \                     /     |     \                                          |
                            |       |>CopyOnWriteArrayList    |      \                   /      |      \                                         |>TreeSet
                   RoleList<|       |                         |     BlockingDequeue(I)<-/       |        TransferQueue(I)                        |
                            |       |>Vector                  |     |                           |                   |                            |>ConcurrentSkipListSet
         RoleUnresolvedList<|           |                     |     |>LinkedBloickingDeque      |                   |>LinkedTransferQueue
                                        |>Stack               |                                 |                                                  
                                                   ArrayDeque<|                                 |>DelayQueue                 
                                                              |                                 |
                                          ConcurrentListDeque<|                                 |>ArrayBlockingQueue
                                                                                                |
                                                                                                |>LinkedBlockingQeque
                                                                                                |
                                                                                                |>PriorityBlockingQueue
                                                                                                |
                                                                                                |>SynchronousQueue


                                            Map(I)
                                               |
        +--------------------------------------------------------------------------------------------+
        |                           |                                  |                             |
        |>WeakHashMap           SortedMap(I)                    ConcurrentMap(I)            javafx.collections.ObservableMap(I)
        |                           |                           |         |                           |
        |>IdentityHashMap         NavigableMap(I)               |         |>ConcurrentHashMap         |>javafx.beans.value.ObservableMapValue(I)
        |                           |          \                |
        |>Hashtable              TreeMap        \               |
        |    |                              ConcurrentNavigableMap(I)
        |    |>Properties                           |
        |                                           |>ConcurrentSkipListMap
        |>HashMap                                   
            |
            |>LinkedHashMap