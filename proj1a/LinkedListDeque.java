/**
 * 在DLList基础上修改, 为项目提交内容
 * 重点是理解单sentinel带来的circle拓扑, 所谓circle, 就是:
 * sentinel的next一定指向第一个元素, 第一个元素的prev一定指向sentinel;
 * sentinel的prev一定指向最后一个元素, 最后一个元素的next一定指向sentinel.
 * sentinel即是头也是尾, 所以其实把首尾分开,分别用sentF,sentB应当会好理解一点的.
 * <p>
 * 这里来观察单sentinel的circle拓扑.
 * 以removeFirst()为例, 拿掉一个头部元素造成"断裂", 你有两个动作要去做:
 * 1.找到第二个元素, 让sentinel的next指向它   sentinel.next = sentinel.next.next;
 * 2.让二个元素的prev指向sentinel, 把"断裂"重新接好     sentinel.next.prev = sentinel;
 * 当链表已有多个元素时这很好想, 但考虑链表只有一个元素时, 这会发生什么?
 * 此时链表的第二个元素是谁呢? 就是sentinel //  sentinel.next.next就是sentinel啊
 * 1.sentinel.next = sentinel.next.next; 让sentinel的next指向它自身
 * 2.sentinel.next.prev = sentinel; 让第二个元素(sentinel自身)的prev指向它自身
 * 这样又回到了链表的最初形态, sentinel单节点形成自我circle.
 * 所以, 链表只有一个元素并不是一种特殊情况, 不需要单独处理.
 * <p>
 * 发生断裂时, 下一个的prev指向sentinel, sentinel的next指向下一个. 此时的下一个就是sentinel自己,
 * 所以就是sentinel的prev指向sentinel, sentinel的next指向sentinel.
 * <p>
 * 这个circle是不允许断掉的.
 * <p>
 * <p>
 * <p>
 * 再来看一下addFirst()发生了什么, 要在头部挤进去一个东西, 你有三个动作要去做.
 * 1.造一个节点, 单看这个节点, 它的prev指向sentinel, 它的next指向sentinel.next即原本的第一个节点,
 * 这个节点自己已经准备好了, 你还需要维护两个指针, 让链表前后指向正确, 闭合circle.
 * 2.让sentinel.next(此时指向要让到第二位的那个节点)的prev指向新造的节点
 * 3.让sentinel.next指向新造的节点
 * <p>
 * 同样地, 看看当链表为空时, 这三个动作是什么情况.
 * 1. Node p = new Node(i, sentinel, sentinel.next);   新造的节点prev也指向sentinel, next也指向sentinel,
 * 这很正确. 因为此时, 新造节点也是尾节点, 它的next就应该指向sentinel.
 * 2. sentinel.next.prev = p;  要让到第二位的节点(此时就是sentinel节点)的prev指向新造节点, 即sentinel的prev指向新造节点
 * 3. sentinel.next = p;   sentinel.next指向新造节点
 * 其实, 就是从原本的sentinel自循环变为了两个节点的一个circle. sentinel充当了一个"要让到第二位置的节点", 这与
 * 本来就有多个元素的链表在表头插入元素的动作是一样的, 空表也不是addFirst()的特殊情况, 无需特殊处理.
 * <p>
 * <p>
 * <p>
 * 在对表头进行增删操作的时候, sentinel总是在必要的时候扮演那个你需要找到的那个"处在第二位置"或者"要让到第二位置"的那个节点.
 * 在对表尾进行增删操作的时候也是一样, "处在倒数第二位"或者"要让到倒数第二位".
 * <p>
 * 本质上看, 其实就是sentinel一个节点强行当做了两个哨兵来用, 又当头又当尾形成了circle拓扑结构. 可以在想像中将sentinel节点框
 * [prev/item/next]从item的正中间一分为二, 那左边就是尾巴, 右边就是头.
 * <p>
 * 遗留问题:
 * 1. 递归还是不会写
 * 2. 啥时候private, 尤其是内部类里的成员和方法, 这是怎么算的?
 *
 * @param <T>
 */
public class LinkedListDeque<T> {
    private Node sentinel;
    private int size;

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
        Node p = new Node(i, sentinel, sentinel.next);
        sentinel.next.prev = p;
        sentinel.next = p;
        size++;
    }


    public void addLast(T i) {
        Node p = new Node(i, sentinel.prev, sentinel);
        sentinel.prev.next = p;
        sentinel.prev = p;
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
        Node p = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return p.item;
    }

    /**
     * 删除并返回表尾元素
     *
     * @return 表头元素的item, 空表则返回null
     */

    public T removeLast() {
        if (isEmpty()) {
            return null;
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
     * @return item
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


    private class Node {
        private Node prev;
        private T item;
        private Node next;

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
