/***
 * 实现的有些丑陋, 方法之间逻辑感觉有耦合, 尽力做到了考虑全面.
 * 这是将要提交的版本
 * 无论这个贪吃蛇怎么转, 永远只有那么几个情况
 * 1. 头在尾后面, 中间有空的. 此时将其看作两段.
 * 2. 头在尾前面, 中间是连续的. 此时它是一个连贯的数组
 * 3. 头在尾前面一格, 所有格子满员, 即将发生扩容. 此时size == items.length, 为头尾指针位置的特殊情况.
 *
 * 只要指针位置处理的干净, 就不容易出错.
 */


public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int RFACTOR = 2;
    private static final double RFACTOR_REDUCE = 0.5;
    private int atLeastCount = 0;

    /**
     * 构造空ArrayDeque
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = items.length - 1;
        nextLast = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }


    public void addLast(T x) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        // 这一句不能放在函数体最后, 即当连续addLast一直到满员时, 不能让nextLast转一圈回到原位
        // 后面resize和minusOne的所有逻辑, 都默认满员时的指针不发生转向, 依此来计算出的前后段个数才是正确的
        if (nextLast == items.length) {
            nextLast = 0;
        }
        items[nextLast] = x;
        size++;
        nextLast++;
    }

    public void addFirst(T x) {
        // 若已满员, 先扩容为两倍, 其实这个动作可以放在下面.
        // 即当存满后立即扩容. 虽然能为minusOne()和resize()的处理带来一些方便(少一个头尾指针的特殊情况),
        // 但这牺牲了数据结构行为上的合理性
        if (size == items.length) {
            resize(items.length * RFACTOR);
        }
        if (nextFirst == -1) {
            nextFirst = items.length - 1;
        }
        items[nextFirst] = x;
        size++;
        nextFirst--;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextFirst++;
        if (nextFirst == items.length) {
            nextFirst = 0;
        }
        size--;
        T temp = items[nextFirst];
        items[nextFirst] = null;

        // 若减少一员后, size小于了最小许可放置数, 则items容量减半. 底层items重置
        if (size < atLeastCount) {
            resize((int) (items.length * RFACTOR_REDUCE));
        }
        return temp;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextLast--;
        if (nextLast == -1) {
            nextLast = items.length - 1;
        }
        size--;
        T temp = items[nextLast];
        items[nextLast] = null;

        // 若减少一员后, size小于了最小许可放置数, 则items容量减半. 底层items重置
        if (size < atLeastCount) {
            resize((int) (items.length * RFACTOR_REDUCE));
        }
        return temp;
    }

    /**
     * resize()的时机有两个:
     * 1.满员导致扩容,
     * 2.remove导致收缩: remove又分两种情形 1.发生转向 2.未发生转向
     *
     * @param capacity 新的items容量
     */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];

        // 只有remove发生转向(此时nextFirst < nextLast)时, 才按顺序复制
        // && size != items.length是为了排除满员即将扩容的情况, 因为这时nextFirst 也小于 nextLast
        if (nextFirst < nextLast && size != items.length) {
            System.arraycopy(items, nextFirst + 1, temp, 0, size);
        } else {
            // 满员和remove未转向都分段复制
            int frontCount = size - nextLast;
            int backCount = nextLast;
            System.arraycopy(items, nextFirst + 1, temp, 0, frontCount);
            System.arraycopy(items, 0, temp, frontCount, backCount);
        }

        // 重置items和头尾指针
        items = temp;
        nextLast = size;
        nextFirst = items.length - 1;

        // 重置最小许可放置数, 供remove判断
        if (items.length >= 16) {
            atLeastCount = (int) (items.length * 0.25);
        } else {
            atLeastCount = 0;
        }
    }

    public T get(int index) {
        // 排除列表为空或者违法index的情况
        if (index < 0 || index > size - 1) {
            return null;
        }
        return items[minusOne(index)];
    }

    /**
     * 概念索引转换为底层数组实际索引
     *
     * @param index 概念索引
     * @return 底层数组实际索引
     */
    private int minusOne(int index) {
        // 未转向(两边都有数据,包含满数据的情况)且概念索引超出front部分个数, 则返回index-frontCount
        if (size == items.length || nextFirst >= nextLast) {
            int frontCount = size - nextLast;
            if (index + 1 > frontCount) {
                return index - frontCount;
            }
        }
        // 其他情况:转向,或者未转向且概念索引在front个数之内, 都使用nextFirst定位
        return nextFirst + 1 + index;
    }
}
