/**
 * Created by jug on 1/22/18.
 */
public class DebugExercise3 {
    public static int countTurnips(In in) {
        int totalTurnips = 0;
        while (!in.isEmpty()) {
            String vendor = in.readString();
            String foodType = in.readString();
            double cost = in.readDouble();
            int numAvailable = in.readInt();
            if (foodType.equals("turnip")) {
                int newTotal = totalTurnips + numAvailable;
                totalTurnips = newTotal;
            }
            // 所以这句话是从当前位置开始读到下一个换行符,
            // 也就是说是想读掉当前行的脏数据(如果有的话)和换行符准备开始下一行。
            // 所以当没有脏数据时不要这句也没关系, 因为nextString会吃掉不是String的东西(也就是说也会吃掉换行符)
            in.readLine();
        }
        return totalTurnips;
    }

    public static void main(String[] args) {
        In in = new In("foods.csv");
        System.out.println(countTurnips(in));
    }
}
