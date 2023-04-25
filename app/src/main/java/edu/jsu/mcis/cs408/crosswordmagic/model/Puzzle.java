package edu.jsu.mcis.cs408.crosswordmagic.model;

import java.util.HashMap;
import java.util.HashSet;

public class Puzzle {

    public static final char BLOCK_CHAR = '*';
    public static final char BLANK_CHAR = ' ';

    private HashMap<String, Word> words;
    private HashSet<String> guessed;

    private String name, description;
    private Integer height, width;

    private Character[][] letters;
    private Integer[][] numbers;

    private boolean solved = false;

    private StringBuilder cluesAcrossBuffer, cluesDownBuffer;

    public Puzzle(HashMap<String, String> params) {

        this.name = params.get("name");
        this.description = params.get("description");
        this.height = Integer.parseInt(params.get("height"));
        this.width = Integer.parseInt(params.get("width"));

        guessed = new HashSet<>();
        words = new HashMap<>();

        letters = new Character[height][width];
        numbers = new Integer[height][width];

        cluesAcrossBuffer = new StringBuilder();
        cluesDownBuffer = new StringBuilder();

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                letters[i][j] = BLOCK_CHAR;
                numbers[i][j] = 0;

            }

        }

    }

    public void addWord(Word word) {

        String key = (word.getBox() + word.getDirection().toString());

        /* add to collection */

        words.put(key, word);

        /* get properties */

        int row = word.getRow();
        int column = word.getColumn();
        int length = word.getWord().length();

        /* add to boxes */

        numbers[row][column] = word.getBox();

        /* clear letter boxes */

        for (int i = 0; i < length; ++i) {

            letters[row][column] = BLANK_CHAR;

            if (word.isAcross())
                column++;
            else if (word.isDown())
                row++;

        }

        /* append clue (across or down) to corresponding StringBuilder */

        if (word.isAcross())
            cluesAcrossBuffer.append(word.getBox()).append(": ").append(word.getClue()).append("\n");

        else if (word.isDown())
            cluesDownBuffer.append(word.getBox()).append(": ").append(word.getClue()).append("\n");

        /* add word to grid (for development only!) */

        addWordToGrid(key);

    }

    public Word guess(Integer num, String guess) {

        Word result = null;

        /* create keys for across/down word(s) at specified box number */

        String acrossKey = num + WordDirection.ACROSS.toString();
        String downKey = num + WordDirection.DOWN.toString();

        /* get word(s) from collection (will return null for non-existent words!) */

        Word across = words.get(acrossKey);
        Word down = words.get(downKey);

        /* compare guess to word(s); if it matches, and if it has not already been solved, assign word to "result" and call "addWordToGrid()" */

        if (across != null) {
            if (across.getWord().equals(guess)) {
                result = across;
                addWordToGrid(acrossKey);
            }
        }

        else if (down != null) {
            if (down.getWord().equals(guess)) {
                result = down;
                addWordToGrid(downKey);
            }
        }

        /* check if any blank cells remain in "letters"; if not, the puzzle is solved, so set "solved" to true */

        solved = true;

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                if (letters[i][j] == BLANK_CHAR) {
                    solved = false;
                }

            }

        }

        /* return reference to guessed word (so it can be added to the database) */

        return result;

    }

    public void addWordToGrid(String key) {

        /* get word from collection; add key to set of guessed words */

        Word w = words.get(key);

        guessed.add(key);

        /* get word properties and add letters to "letters" array */

        int row = w.getRow();
        int column = w.getColumn();
        int length = w.getWord().length();

        String word = w.getWord();

        numbers[row][column] = w.getWord();
        // add for loop
        for (int i = 0; i < length; ++i) {

            letters[row][column] = word;

            if (direction.isAcross())
                column++;
            else if (word.isDown())
                row++;

        }

    }

    public Word getWord(String key) {
        return words.get(key);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getCluesAcross() {
        return cluesAcrossBuffer.toString();
    }

    public String getCluesDown() {
        return cluesDownBuffer.toString();
    }

    public int getNumWords() {
        return words.size();
    }

    public Character[][] getLetters() {
        return letters;
    }

    public Integer[][] getNumbers() {
        return numbers;
    }

    public boolean isSolved() {
        return solved;
    }

}