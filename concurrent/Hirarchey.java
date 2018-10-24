

                                                                    Collection(I)
                                                                           |
                                    +-----------------------------------------------------------------------------------------+
                                    |                                      |                                                  |
                                List(I)                                Queue(I)                                              Set(I)
                                /\                                    /        \                                            /      \
                                |                                    /          \                                          /        \
javax.management.relation.+-----|>ArrayList                         /            \                              HashSet <-/          \->SortedSet(I) 
                         |      |                                  /              \                                 |                   |
           AttributeList<|      |>LinkedList ---------> Dequeue(I)                  BlockingQueue(I)                |>LinkedHashSet     |>NaviganleSet(I)
                         |      |                         |     \                     /     |     \                                          |
                RoleList<|      |>CopyOnWriteArrayList    |      \                   /      |      \                                         |>TreeSet
                         |      |                         |     BlockingDequeue(I)<-/       |        TransferQueue(I)                        |
      RoleUnresolvedList<|      |>Vector                  |     |                           |                   |                            |>ConcurrentSkipListSet
                                    |                     |     |>LinkedBloickingDeque      |                   |>LinkedTransferQueue
                                    |>Stack               |                                 |                                                  
                                               ArrayDeque<|                                 |>DelayQueue                 
                                                          |                                 |
                                      ConcurrentListDeque<|                                 |>ArrayBlockingQueue
                                                                                            |
                                                                                            |>LinkedBlockingDeque
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
        |>IdentityHashM         NavigableMap(I)                 |         |>ConcurrentHashMap         |>javafx.beans.value.ObservableMapValue(I)
        |                           |          \                |
        |>Hashtable              TreeMap        \               |
        |    |                              ConcurrentNavigableMap(I)
        |    |>Properties                           |
        |                                           |>ConcurrentSkipListMap
        |>HashMap                                   
            |
            |>LinkedHashMap