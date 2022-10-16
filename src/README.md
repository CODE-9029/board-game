Displaying the data: 
At the top border, we have a text box showing the current size. Among them, "size:" is not editable, and the number is editable. There is a difficulty selection button below it. On the right side of the window there are buttons for creating a new game and buttons for checking. In the center of the window is our puzzle.
There is a MaruekeGrid object in the MarupekeGUI class to get the randomly generated Grid.Obtain the data of each Tile through the TileGUI class.
Update Tile status through switch in TileGUI.

Editing the data:
Changing the number in the top border can change the size of the Grid. 
Selecting easy and hard on the left border will generate grids of different difficulty. 
Click on the editable Tile to switch its state between X, O, and Blank in sequence.

Optional Extras:
Difficulty optional.
If you choose easy difficulty, more Solid tiles will be generated. If you choose hard difficulty, Solid tiles will be less.
