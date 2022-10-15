package synthesizer;

import java.util.HashSet;

public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this divsion operation into an int. For better
        //       accuracy, use the Math.round() function before casting.
        //       Your buffer should be initially filled with zeros.
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buffer.enqueue(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise.
     *
     * 一个GuitarString对象实例化后, buffer中所有的double元素都被置为了0.0, pluck()的作用是
     * 让buffer中所有的double都用一个-0.5到0.5之间的随机数来覆盖, 就好像是在buffer中填写乐谱.
     *
     * 即使不要求所有随机数必须不同, 也无法使用增强for循环来取覆盖值, 增强for循环将结构中的元素
     * 一一取出来遍历, 但在循环体内每个元素都是传参的性质, 形参被重新赋值不能改变实参的值或整体指向.
     */
    public void pluck() {
        // Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each other.
        HashSet<Double> doubles = new HashSet<>();
        while (doubles.size() < buffer.capacity()) {
            doubles.add(Math.random() - 0.5);
        }
        for (Double aDouble : doubles) {
            buffer.dequeue();
            buffer.enqueue(aDouble);
        }
    }

    /* 下面两个函数的作用可通过看TestGuitarString.java中的测试用例代码来理解.
     *
     * Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     *
     * 这个是用来演奏完之后通过Karplus-Strong算法更新buffer的, 更新的动作就是pop第一个元素与第二个元素进行计算,
     * 计算后的值enqueue(push)到buffer的末尾. 下一次演奏就再调用sample().
     */
    public void tic() {
        // Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().
        buffer.enqueue((buffer.dequeue() + buffer.peek()) / 2 * DECAY);
    }

    /* Return the double at the front of the buffer.
     *
     * 这个是用来演奏的, 即StdAudio.play(aGuitarString.sample()), 简单把第一个元素peek()出来就好
     */
    public double sample() {
        // Return the correct thing.
        return buffer.peek();
    }
}
