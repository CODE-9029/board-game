package marupeke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Marupeke {
    private MarupekeTile[][] grid;
    private ArrayList<String> illegalTiles = new ArrayList();

    public Marupeke(int size) {
        if (size >= 10) size = 10;
        else if (size < 3) size = 3;

        grid = new MarupekeTile[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //Set all tiles to be editable and blank
                grid[i][j] = new MarupekeTile(true, State.BLANK);
            }
        }
    }

    public static Marupeke randomPuzzle(int size, Difficulty difficulty) {
        int numFill = 0;
        int numX = 0;
        int numO = 0;

        switch (difficulty) {
            //easy means more solid tiles than hard, then players don't need to consider about these tiles too much
            case Easy: {
                numFill = size * size / 4;
                numX = size * size / 8;
                numO = size * size / 8;
            }
            break;
            case Hard: {
                numFill = size * size / 8;
                numX = size * size / 4;
                numO = size * size / 8;
            }
            break;
        }

        int total = numFill + numX + numO;
        if (total > (size * size) / 2) {
            System.err.println("The total exceeds the number of playable cells");
            return null;
        }

        boolean isLegal = false;
        boolean finishAble = false;
        Marupeke finalGrid = new Marupeke(size);
        //Only the size of 3 can ensure that the puzzle can be completed, if the size is greater than 3,
        // the isFinishAble is likely to be executed for too long,
        // so if the size is greater than 3, it will not be tested whether it can be completed
        if (size == 3) {
            while (isLegal == false || finishAble == false) {
                Marupeke newGrid = new Marupeke(size);
                ArrayList<int[]> cells = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        int[] cell = new int[2];
                        cell[0] = i;
                        cell[1] = j;

                        cells.add(cell);
                    }
                }

                Collections.shuffle(cells);

                for (int i = 0; i < numFill; i++) {
                    newGrid.setSolid(cells.get(0)[0], cells.get(0)[1]);
                    cells.remove(0);
                }

                for (int i = 0; i < numX; i++) {
                    newGrid.setX(cells.get(0)[0], cells.get(0)[1], false);
                    cells.remove(0);
                }

                for (int i = 0; i < numO; i++) {
                    newGrid.setO(cells.get(0)[0], cells.get(0)[1], false);
                    cells.remove(0);
                }

                isLegal = newGrid.isLegal();
                finishAble = newGrid.isFinishAble();

                if (isLegal && finishAble) finalGrid = newGrid;
            }
        }
        else {
            while (isLegal == false) {
                Marupeke newGrid = new Marupeke(size);
                ArrayList<int[]> cells = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        int[] cell = new int[2];
                        cell[0] = i;
                        cell[1] = j;

                        cells.add(cell);
                    }
                }

                Collections.shuffle(cells);

                for (int i = 0; i < numFill; i++) {
                    newGrid.setSolid(cells.get(0)[0], cells.get(0)[1]);
                    cells.remove(0);
                }

                for (int i = 0; i < numX; i++) {
                    newGrid.setX(cells.get(0)[0], cells.get(0)[1], false);
                    cells.remove(0);
                }

                for (int i = 0; i < numO; i++) {
                    newGrid.setO(cells.get(0)[0], cells.get(0)[1], false);
                    cells.remove(0);
                }

                isLegal = newGrid.isLegal();

                if (isLegal) finalGrid = newGrid;
            }
        }

        return finalGrid;
    }

    public boolean setSolid(int x, int y) {
        MarupekeTile tile = grid[x][y];
        boolean wasEditable = (tile.isEditable == true);

        if (wasEditable) {
            tile.state = State.SOLID;
            tile.isEditable = false;
        }

        return wasEditable;
    }

    public boolean setX(int x, int y, boolean canEdit) {
        MarupekeTile tile = grid[x][y];
        boolean wasEditable = (tile.isEditable == true);

        if (wasEditable) {
            tile.state = State.X;
            tile.isEditable = canEdit;
        }

        return wasEditable;
    }

    public boolean setO(int x, int y, boolean canEdit) {
        MarupekeTile tile = grid[x][y];
        boolean wasEditable = (tile.isEditable == true);

        if (wasEditable) {
            tile.state = State.O;
            tile.isEditable = canEdit;
        }

        return wasEditable;
    }

    public boolean unmark(int x, int y) {
        MarupekeTile tile = grid[x][y];

        if (tile.isEditable) {
            tile.state = State.BLANK;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                switch (grid[i][j].state) {
                    case X:
                        builder.append("X");
                        break;
                    case O:
                        builder.append("O");
                        break;
                    case SOLID:
                        builder.append("#");
                        break;
                    case BLANK:
                        builder.append("_");
                        break;
                }
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    public MarupekeTile[][] getGrid() {
        return grid;
    }

    public boolean isLegal() {
        boolean legal = true;

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
                MarupekeTile tile = grid[x][y];

                for (Direction direction : Direction.values()) {
                    try {
                        if (tile.state == State.X || tile.state == State.O) {
                            if (grid[x + direction.x][y + direction.y].state == tile.state) {
                                if (grid[x + direction.x * 2][y + direction.y * 2].state == tile.state) {
                                    illegalTiles.add("There is a row of 3 " + tile.state.toString() + "'s");
                                    legal = false;
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (legal == false) printIllegalities();

        return legal;
    }

    public boolean isFinishAble() {
        //Only the size of 3 can ensure that the puzzle can be completed, if the size is greater than 3,
        // the program is likely to be executed for too long,
        // so if the size is greater than 3, it will not be tested whether it can be completed
        boolean finishAble = false;
        int h = 1;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
                MarupekeTile tile = grid[x][y];
                if (tile.state == State.BLANK){
                    tile.numb = h;
                    tile.state = State.X;
                    h++;
                }
            }
        }
        int i = 1;
        int j = 1;
        int k = 1;
        while (k < h) {
            if (i > 0){
                for (int x = 0; x < grid.length; x++) {
                    for (int y = 0; y < grid.length; y++) {
                        MarupekeTile tile = grid[x][y];
                        if (tile.numb == i){
                            tile.state = State.O;
                            boolean legal = true;

                            for (int a = 0; a < grid.length; a++) {
                                for (int b = 0; b < grid.length; b++) {
                                    MarupekeTile tile1 = grid[a][b];

                                    for (Direction direction : Direction.values()) {
                                        try {
                                            if (tile1.state == State.X || tile1.state == State.O) {
                                                if (grid[a + direction.x][a + direction.y].state == tile1.state) {
                                                    if (grid[b + direction.x * 2][b + direction.y * 2].state == tile1.state) {
                                                        illegalTiles.add("There is a row of 3 " + tile1.state.toString() + "'s");
                                                        legal = false;
                                                    }
                                                }
                                            }
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                        }
                                    }
                                }
                            }
                            if (legal) {
                                finishAble = legal;
                                for (int a = 0; a < grid.length; a++) {
                                    for (int b = 0; b < grid.length; b++) {
                                        MarupekeTile tile1 = grid[a][b];
                                        if (tile1.numb != 0){
                                            tile1.state = State.BLANK;
                                        }
                                    }
                                }
                                return finishAble;
                            }
                        }
                    }
                }
                i--;
            }
            if (i == 0) {
                for (int l = 1; l < j; l++) {
                    for (int x = 0; x < grid.length; x++) {
                        for (int y = 0; y < grid.length; y++) {
                            MarupekeTile tile = grid[x][y];
                            if (tile.numb == l){
                                tile.state = State.X;
                                boolean legal = true;

                                for (int a = 0; a < grid.length; a++) {
                                    for (int b = 0; b < grid.length; b++) {
                                        MarupekeTile tile1 = grid[a][b];

                                        for (Direction direction : Direction.values()) {
                                            try {
                                                if (tile1.state == State.X || tile1.state == State.O) {
                                                    if (grid[a + direction.x][a + direction.y].state == tile1.state) {
                                                        if (grid[b + direction.x * 2][b + direction.y * 2].state == tile1.state) {
                                                            illegalTiles.add("There is a row of 3 " + tile1.state.toString() + "'s");
                                                            legal = false;
                                                        }
                                                    }
                                                }
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                            }
                                        }
                                    }
                                }
                                if (legal) {
                                    finishAble = legal;
                                    for (int a = 0; a < grid.length; a++) {
                                        for (int b = 0; b < grid.length; b++) {
                                            MarupekeTile tile1 = grid[a][b];
                                            if (tile1.numb != 0){
                                                tile1.state = State.BLANK;
                                            }
                                        }
                                    }
                                    return finishAble;
                                }
                            }
                        }
                    }
                }
                j++;
                if (j==k){
                    for (int x = 0; x < grid.length; x++) {
                        for (int y = 0; y < grid.length; y++) {
                            MarupekeTile tile = grid[x][y];
                            if (tile.numb == k){
                                tile.state = State.X;
                                boolean legal = true;

                                for (int a = 0; a < grid.length; a++) {
                                    for (int b = 0; b < grid.length; b++) {
                                        MarupekeTile tile1 = grid[a][b];

                                        for (Direction direction : Direction.values()) {
                                            try {
                                                if (tile1.state == State.X || tile1.state == State.O) {
                                                    if (grid[a + direction.x][a + direction.y].state == tile1.state) {
                                                        if (grid[b + direction.x * 2][b + direction.y * 2].state == tile1.state) {
                                                            illegalTiles.add("There is a row of 3 " + tile1.state.toString() + "'s");
                                                            legal = false;
                                                        }
                                                    }
                                                }
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                            }
                                        }
                                    }
                                }
                                if (legal) {
                                    finishAble = legal;
                                    for (int a = 0; a < grid.length; a++) {
                                        for (int b = 0; b < grid.length; b++) {
                                            MarupekeTile tile1 = grid[a][b];
                                            if (tile1.numb != 0){
                                                tile1.state = State.BLANK;
                                            }
                                        }
                                    }
                                    return finishAble;
                                }
                            }
                        }
                    }
                    i = 1;
                    k++;
                    for (int x = 0; x < grid.length; x++) {
                        for (int y = 0; y < grid.length; y++) {
                            MarupekeTile tile = grid[x][y];
                            if (tile.numb == k){
                                tile.state = State.O;
                                boolean legal = true;

                                for (int a = 0; a < grid.length; a++) {
                                    for (int b = 0; b < grid.length; b++) {
                                        MarupekeTile tile1 = grid[a][b];

                                        for (Direction direction : Direction.values()) {
                                            try {
                                                if (tile1.state == State.X || tile1.state == State.O) {
                                                    if (grid[a + direction.x][a + direction.y].state == tile1.state) {
                                                        if (grid[b + direction.x * 2][b + direction.y * 2].state == tile1.state) {
                                                            illegalTiles.add("There is a row of 3 " + tile1.state.toString() + "'s");
                                                            legal = false;
                                                        }
                                                    }
                                                }
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                            }
                                        }
                                    }
                                }
                                if (legal) {
                                    finishAble = legal;
                                    for (int a = 0; a < grid.length; a++) {
                                        for (int b = 0; b < grid.length; b++) {
                                            MarupekeTile tile1 = grid[a][b];
                                            if (tile1.numb != 0){
                                                tile1.state = State.BLANK;
                                            }
                                        }
                                    }
                                    return finishAble;
                                }
                            }
                        }
                    }
                    j = 1;
                }
                if (j != k) {
                    for (int x = 0; x < grid.length; x++) {
                        for (int y = 0; y < grid.length; y++) {
                            MarupekeTile tile = grid[x][y];
                            if (tile.numb == j){
                                tile.state = State.O;
                                boolean legal = true;

                                for (int a = 0; a < grid.length; a++) {
                                    for (int b = 0; b < grid.length; b++) {
                                        MarupekeTile tile1 = grid[a][b];

                                        for (Direction direction : Direction.values()) {
                                            try {
                                                if (tile1.state == State.X || tile1.state == State.O) {
                                                    if (grid[a + direction.x][a + direction.y].state == tile1.state) {
                                                        if (grid[b + direction.x * 2][b + direction.y * 2].state == tile1.state) {
                                                            illegalTiles.add("There is a row of 3 " + tile1.state.toString() + "'s");
                                                            legal = false;
                                                        }
                                                    }
                                                }
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                            }
                                        }
                                    }
                                }
                                if (legal) {
                                    finishAble = legal;
                                    for (int a = 0; a < grid.length; a++) {
                                        for (int b = 0; b < grid.length; b++) {
                                            MarupekeTile tile1 = grid[a][b];
                                            if (tile1.numb != 0){
                                                tile1.state = State.BLANK;
                                            }
                                        }
                                    }
                                    return finishAble;
                                }
                            }
                        }
                    }
                    i = 1;
                }
            }
        }
        for (int a = 0; a < grid.length; a++) {
            for (int b = 0; b < grid.length; b++) {
                MarupekeTile tile1 = grid[a][b];
                if (tile1.numb != 0){
                    tile1.state = State.BLANK;
                }
            }
        }
        return finishAble;
    }

    private void printIllegalities() {
        System.out.println("The illegalities in the grid:");
        for (String message : illegalTiles) {
            System.out.println("- " + message);
        }
    }

    public boolean isPuzzleComplete() {
        int blankCount = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j].state == State.BLANK) {
                    blankCount++;
                }
            }
        }

        return (blankCount == 0 && isLegal());
    }

}
