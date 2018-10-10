

                                    Collection(I)
                                           |
    +-----------------------------------------------------------------------------------------+
    |                                      |                                                  |
List(I)                                Queue(I)                                              Set(I)
/\                                    /        \                                            /      \
|                                    /          \                                          /        \
|>ArrayList                         /            \                              HashSet <-/          \->SortedSet(I) 
|                                  /              \                                 |                   |
|>LinkedList ---------> Dequeue(I)                  BlockingQueue(I)                |>LinkedHashSet     |>NaviganleSet(I)
|                         |     \                     /     |     \                                          |
|>CopyOnWriteArrayList    |      \                   /      |      \                                         |>TreeSet
|                         |     BlockingDequeue(I)<-/       |        TransferQueue(I)                        |
|>Vector                  |     |                           |                   |                            |>ConcurrentSkipListSet
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