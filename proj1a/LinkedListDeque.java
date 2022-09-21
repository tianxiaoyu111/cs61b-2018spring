/**
 * 在DLList基础上修改, 为项目提交内容
 *
 * @param <T>
 */

public class LinkedListDeque<T> {
    public Node sentinel;
    public int size;

    /**
     * 空表构造方法
     */
    public LinkedListDeque() {
        sentinel = new Node();
        sentinel.next = sentinel;   // 指向表头元素, 空表时则指向自身
        sentinel.prev = sentinel;   // 指向表尾元素, 空表时则指向自身
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        Node ptr = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    public void addFirst(T i) {
        if (isEmpty()) {
            sentinel.next = new Node(i, sentinel, sentinel);
            sentinel.prev = sentinel.next;
        } else {
            Node p = new Node(i, sentinel, sentinel.next);
            sentinel.next.prev = p;
            sentinel.next = p;
        }
        size++;
    }


    public void addLast(T i) {
        if (isEmpty()) {
            sentinel.prev = new Node(i, sentinel, sentinel);
            sentinel.next = sentinel.prev;
        } else {
            Node p = new Node(i, sentinel.prev, sentinel);
            sentinel.prev.next = p;
            sentinel.prev = p;
        }
        size++;
    }


    /**
     * 删除并返回表头元素
     *
     * @return 表头元素的item, 空表则返回null
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size == 1) {
            sentinel.prev = sentinel;
        }
        Node p = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return p.item;
    }

    /**
     * 删除并返回表尾元素, 空表则返回null
     *
     * @return 表头元素的item, 空表则返回null
     */

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size == 1) {
            sentinel.next = sentinel;
        }
        Node p = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return p.item;
    }

    /**
     * 获取index位置上元素的值
     *
     * @param index 元素位置索引
     * @return
     */
    public T get(int index) {
        // 空表时, 即size为0时, 此逻辑运算必定为真. 即任意一个整数要么小于0, 要么大于-1
        if (index < 0 || index > size - 1) {
            return null;
        }
        // p始终指向i号元素, i最终等于index, 即p最终指向index号元素
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    private T getRecursiveHelp(Node start, int index) {
        if (index == 0) {
            return start.item;
        } else {
            return getRecursiveHelp(start.next, index - 1);
        }
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelp(sentinel.next, index);
    }


    public class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(T i, Node nodeP, Node nodeN) {
            item = i;
            prev = nodeP;
            next = nodeN;
        }

        /**
         * 哨兵节点构造方法
         */
        public Node() {
        }
    }
}
