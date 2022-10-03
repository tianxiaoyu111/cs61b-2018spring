public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> temp = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            temp.addLast(word.charAt(i));
        }
        return temp;
    }

    public boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        }
        Deque<Character> d = wordToDeque(word);
        String temp = "";
        for (int i = 0; i < word.length(); i++) {
            temp += d.removeLast();     // 合理运用队列特性, 只需让字符从队尾出, 拼接成反转字符串与原word比对即可
        }
        return temp.equals(word);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() <= 1) {
            return true;
        }
        Deque<Character> d = wordToDeque(word);
        while (d.size() >= 2) {
            if (!cc.equalChars(d.removeFirst(), d.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
