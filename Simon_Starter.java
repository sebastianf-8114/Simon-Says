import java.awt.Color;
import java.util.Random;
import javalib.funworld.*;
import tester.Tester;
import javalib.worldimages.*;

// Represents a game of Simon Says
class SimonWorld extends World {
  int sceneSize;
  Random rand;
  ILoButton randomButtonList;
  ILoButton lightButtonList;
  Button greenButton;
  Button redButton;
  Button yellowButton;
  Button blueButton;
  boolean flashing;
  boolean justFlashed;
  boolean gameEnded;

  // a general constructor that can be called with random
  SimonWorld() {
    this(new Random());
  }

  // The constructor for use in testing, with a specified Random object
  SimonWorld(Random rand) { 
    this.sceneSize = 1000;
    this.greenButton = new Button(Color.GREEN, 250, 250);
    this.redButton = new Button(Color.RED, 750, 250);
    this.yellowButton = new Button(Color.YELLOW, 250, 750);
    this.blueButton = new Button(Color.BLUE, 750, 750);
    this.rand = rand;
    this.randomButtonList = new ConsLoButton(this.intToButton(this.rand.nextInt(4)), 
        new MtLoButton());
    this.lightButtonList = this.randomButtonList;
    this.flashing = true;
    this.justFlashed = false;
    this.gameEnded = false;
  }

  // a very general constructor
  SimonWorld(int sceneSize, Random rand,
      Button greenButton, Button redButton, Button yellowButton, Button blueButton,
      ILoButton randomButtonList, ILoButton lightButtonList,  boolean flashing, 
      boolean justFlashed, boolean gameEnded) {
    this.sceneSize = sceneSize;
    this.rand = rand;
    this.greenButton = greenButton;
    this.redButton = redButton;
    this.yellowButton = yellowButton;
    this.blueButton = blueButton;
    this.randomButtonList = randomButtonList;
    this.lightButtonList = lightButtonList;
    this.flashing = flashing;
    this.justFlashed = justFlashed;
    this.gameEnded = gameEnded;
  }

  /* Template:
   * Fields:
   * sceneSize          -- int
     rand                --Random 
     this.randomButtonList    --ILoButton
     this.lightButtonList      --ILoButton
     this.greenButton         --Button 
     this.redButton           --Button 
     this.yellowButton        --Button 
     this.blueButton          --Button 
     this.flashing            --boolean 
     this.justFlashed         --boolean
     this.gameEnded           -- boolean
     Methods:
     makeScene()              -- WorldScene
     onTick()                 -- World
     lastScene(String msg)    -- WorldScene
     onMouseClicked(Posn pos) -- SimonWorld
     intToButton(int num)     -- Button
     Methods of Fields:
     this.greenButton.drawDark()      -- WorldImage
     this.redButton.drawDark()      -- WorldImage
     this.greenButton.getX()      -- int
     this.greenButton.getY()      -- int
     this.redButton.getX()      -- int
     this.redButton.getY()      -- int
     this.yellowButton.getX()      -- int
     this.yellowButton.getY()      -- int
     this.blueButton.getX()      -- int
     this.blueButton.getY()      -- int
     this.lightButtonList.firstX()      -- int
     this.lightButtonList.firstY()      -- int
     this.lightButtonList.length()     -- int
     this.randomButtonList.addToEnd(Button)      -- ILoButton
     this.lightButtonList.regenerateList(IILoButton)     -- ILoButton
     this.lightButtonList.removeLight()      -- ILoButton
     this.greenButton.sameLocation(pos)      -- boolean
     this.lightButtonList.sameLists(Button)      -- boolean
     this.redButton.sameLocation(pos)      -- boolean
     this.yellowButton.sameLocation(pos)      -- boolean
     this.blueButton.sameLocation(pos)      -- boolean
   */


  // draws the scene
  public WorldScene makeScene() {
    WorldScene background = new WorldScene(sceneSize, sceneSize);
    if (flashing || justFlashed) {
      return background
          .placeImageXY(this.greenButton.drawDark(), 
              this.greenButton.getX(), this.greenButton.getY())
          .placeImageXY(this.redButton.drawDark(), 
              this.redButton.getX(), this.redButton.getY())
          .placeImageXY(this.yellowButton.drawDark(), this.yellowButton.getX(), 
              this.yellowButton.getY())
          .placeImageXY(this.blueButton.drawDark(), 
              this.blueButton.getX(), this.blueButton.getY())
          .placeImageXY(this.lightButtonList.drawFirst(), 
              this.lightButtonList.firstX(), this.lightButtonList.firstY());
    } else {
      return background
          .placeImageXY(this.greenButton.drawDark(), 
              this.greenButton.getX(), this.greenButton.getY())
          .placeImageXY(this.redButton.drawDark(), 
              this.redButton.getX(), this.redButton.getY())
          .placeImageXY(this.yellowButton.drawDark(), 
              this.yellowButton.getX(), this.yellowButton.getY())
          .placeImageXY(this.blueButton.drawDark(), 
              this.blueButton.getX(), this.blueButton.getY());
    }
  }
  /* Methods of Fields:
    this.greenButton.drawDark()      -- WorldImage
    this.redButton.drawDark()      -- WorldImage
    this.greenButton.getX()      -- int
    this.greenButton.getY()      -- int
    this.redButton.getX()      -- int
    this.redButton.getY()      -- int
    this.yellowButton.getX()      -- int
    this.yellowButton.getY()      -- int
    this.blueButton.getX()      -- int
    this.blueButton.getY()      -- int
    this.lightButtonList.firstX()      -- int
    this.lightButtonList.firstY()      -- int
   */

  // handles ticking of the clock and updating the world if needed
  public World onTick() {  
    if (!this.gameEnded) {
      if ((!this.flashing) && (this.lightButtonList.length() == 0)) {
        ILoButton randomButtomList = 
            this.randomButtonList.addToEnd(this.intToButton(new Random().nextInt(4)));
        return new SimonWorld(sceneSize, rand, 
            this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
            randomButtomList, this.lightButtonList.regenerateList(randomButtomList), 
            true, false, false);
      } else if ((this.flashing) && (this.lightButtonList.length() == 0)) {
        return new SimonWorld(sceneSize, rand, 
            this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
            this.randomButtonList, this.lightButtonList.regenerateList(this.randomButtonList), 
            false, false, false);
      } else if ((this.flashing) && (this.lightButtonList.length() > 0)) {
        return new SimonWorld(sceneSize, rand, 
            this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
            this.randomButtonList, this.lightButtonList.removeLight(), 
            true, false, false);
      } else if (justFlashed) {
        return new SimonWorld(sceneSize, rand, 
            this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
            this.randomButtonList, this.lightButtonList.removeLight(), 
            false, false, false);
      } else {
        return new SimonWorld(sceneSize, rand, 
            this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
            this.randomButtonList, this.lightButtonList, 
            false, false, false);
      }
    } else {
      return this.endOfWorld("You Lost!");
    }
  }
  /* Methods of Fields:
  this.lightButtonList.length()     -- int
  this.randomButtonList.addToEnd(Button)      -- ILoButton
  this.lightButtonList.regenerateList(IILoButton)     -- ILoButton
  this.lightButtonList.removeLight()      -- ILoButton
   */

  // Returns the final scene with the given message displayed
  public WorldScene lastScene(String msg) {
    WorldScene background = new WorldScene(sceneSize, sceneSize);
    WorldImage text = new TextImage(msg, 20, Color.BLACK);
    return background.placeImageXY(text, 500, 500);
  }
  /* Methods of Fields:
     n/a
   */

  // handles mouse clicks and is given the mouse location
  public SimonWorld onMouseClicked(Posn pos) {
    if (this.greenButton.sameLocation(pos) 
        && this.lightButtonList.sameLists(this.greenButton)) {
      return new SimonWorld(this.sceneSize, this.rand, 
          this.greenButton, this.redButton, this.yellowButton, this.blueButton,
          this.randomButtonList, this.lightButtonList, false, true, false);
    } else if (this.redButton.sameLocation(pos) 
        && this.lightButtonList.sameLists(this.redButton)) {
      return new SimonWorld(sceneSize, rand, 
          this.greenButton, this.redButton, this.yellowButton, this.blueButton,
          this.randomButtonList, this.lightButtonList, false, true, false);
    } else if (this.yellowButton.sameLocation(pos) 
        && this.lightButtonList.sameLists(this.yellowButton)) {
      return new SimonWorld(sceneSize, rand, 
          this.greenButton, this.redButton, this.yellowButton, this.blueButton,
          this.randomButtonList, this.lightButtonList, false, true, false);
    } else if (this.blueButton.sameLocation(pos) 
        && this.lightButtonList.sameLists(this.blueButton)) {
      return new SimonWorld(sceneSize, rand, 
          this.greenButton, this.redButton, this.yellowButton, this.blueButton,
          this.randomButtonList, this.lightButtonList, false, true, false);
    } else {
      return new SimonWorld(this.sceneSize, this.rand, 
          this.greenButton, this.redButton, this.yellowButton, this.blueButton, 
          this.randomButtonList, this.lightButtonList, false, false, true);
    }
  }
  /* Methods of Fields:
     this.greenButton.sameLocation(pos)      -- boolean
     this.lightButtonList.sameLists(Button)      -- boolean
     this.redButton.sameLocation(pos)      -- boolean
     this.yellowButton.sameLocation(pos)      -- boolean
     this.blueButton.sameLocation(pos)      -- boolean
   */

  // returns a button given a number between 0-3
  public Button intToButton(int num) {
    if (num == 0) {
      return greenButton;
    } else if (num == 1) {
      return redButton;
    } else if (num == 2) {
      return yellowButton;
    } else {
      return blueButton;
    }
  }
  /* Methods of Fields:
  n/a
   */
}

// Represents a list of buttons
interface ILoButton {

  // determines if the first button of a list and a given list
  boolean sameLists(Button button);

  // returns the last button of the list
  Button lastButton(Button button);

  //adds the button to the end of a list
  ILoButton addToEnd(Button button);

  // determines the length of the list
  int length();

  // remove the first item of a list
  ILoButton removeLight();

  // return the other list if this list is empty
  ILoButton regenerateList(ILoButton other);

  // draws the first image of the list
  WorldImage drawFirst();

  // returns the first buttono's x coordinate of the list
  int firstX();

  // returns the first buttono's y coordinate of the list
  int firstY();
} 

// Represents an empty list of buttons
class MtLoButton implements ILoButton {

  // the  constructor
  MtLoButton() {}

  /* Template:
  Fields:
  n/a
  Methods:
  sameLists(Button)     -- boolean
  addToEnd(Button)     -- boolean
  length()      -- int
  lastButton(Button)     -- Button
  removeLight()     -- ILoButton
  regenerateList(ILoButton)     -- ILoButton
  drawFirst()     -- WorldImage
  firstX()      -- int
  firstY()      -- int
  Methods of Fields:
  n/a
   */

  // adds the button to the end of a list
  public boolean sameLists(Button button) {
    return false;
  }
  /* Methods of Fields:
  n/a
   */

  // adds the button to the end of a list
  public ILoButton addToEnd(Button button) {
    return new ConsLoButton(button, new MtLoButton());
  }

  // determines the length of the list
  public int length() {
    return 0;
  }
  /* Methods of Fields:
  n/a
   */

  // returns the last button of a list
  public Button lastButton(Button button) {
    return button;
  }
  /* Methods of Fields:
  n/a
   */

  // remove the first item of a list
  public ILoButton removeLight() {
    return this;
  }
  /* Methods of Fields:
  n/a
   */

  // return the other list if this list is empty
  public ILoButton regenerateList(ILoButton other) {
    return other;
  }
  /* Methods of Fields:
  n/a
   */

  // draws the first image of the list
  public WorldImage drawFirst() {
    return new EmptyImage();
  }
  /* Methods of Fields:
  n/a
   */

  // returns the first buttono's x coordinate of the list
  public int firstX() {
    return 0;
  }
  /* Methods of Fields:
  n/a
   */

  // returns the first buttono's y coordinate of the list
  public int firstY() {
    return 0;
  }
  /* Methods of Fields:
  n/a
   */
} 

// Represents a non-empty list of buttons
class ConsLoButton implements ILoButton {
  Button first;
  ILoButton rest;

  // the constructor
  ConsLoButton(Button first, ILoButton rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Template:
  Fields:
  this.first    -- Button
  this.rest     -- ILoButton
  Methods:
  sameLists(Button)     -- boolean
  addToEnd(Button)     -- boolean
  length()      -- int
  lastButton(Button)     -- Button
  removeLight()     -- ILoButton
  regenerateList(ILoButton)     -- ILoButton
  drawFirst()     -- WorldImage
  firstX()      -- int
  firstY()      -- int
  Methods of Fields:
  this.rest.length()      -- int
  this.rest.lastButton(button)      -- Button
  this.rest.addToEnd(Button)      -- ILoButton
  this.first.drawLit()      -- WorldImage
  this.first.getX()     -- int
  this.first.getY()     -- int
   */

  // adds the button to the end of a list
  public boolean sameLists(Button button) {
    return this.lastButton(button).sameColor(button);
  }
  /* Methods of Fields:
   * n/a
   */

  // returns the last button of a list
  public Button lastButton(Button button) {
    if (this.rest.length() == 0) {
      return this.rest.lastButton(button);
    } else {
      return this.first;
    }
  }
  /* Methods of Fields:
    this.rest.length()      -- int
    this.rest.lastButton(button)      -- Button
   */

  // adds the button to the end of a list
  public ILoButton addToEnd(Button button) {
    return new ConsLoButton(this.first, this.rest.addToEnd(button));
  }
  /* Methods of Fields:
   * n/a
   */

  // returns the length of a list
  public int length() {
    return 1 + this.rest.length();
  }
  /* Methods of Fields:
    this.rest.length()      -- int
   */

  // remove the first item of a list
  public ILoButton removeLight() {
    return this.rest;
  }
  /* Methods of Fields:
   * n/a
   */

  // return the other list if this list is empty
  public ILoButton regenerateList(ILoButton other) {
    return this;
  }
  /* Methods of Fields:
  n/a
   */

  // draws the first image of the list
  public WorldImage drawFirst() {
    return this.first.drawLit();
  }
  /* Methods of Fields:
  his.first.drawLit()      -- WorldImage
   */

  // returns the first buttono's x coordinate of the list
  public int firstX() {
    return this.first.getX();
  }
  /* Methods of Fields:
  this.first.getX()     -- int
   */

  // returns the first buttono's y coordinate of the list
  public int firstY() {
    return this.first.getY();
  }
  /* Methods of Fields:
  this.first.getY()     -- int
   */
} 

// Represents one of the four buttons you can click
class Button {
  Color color;
  int x;
  int y;

  // the constructor
  Button(Color color, int x, int y) {
    this.color = color;
    this.x = x;
    this.y = y;
  }

  /* Template:
  Fields:
  this.color      -- Color
  this.x      -- int
  this.y      -- int
  Methods:
  drawDark()     -- WorldImage
  drawLit()     -- WorldImage
  drawButton(Color)     -- WorldImage
  sameColor(Button)      -- boolean
  sameColorHelper(Color)      -- boolean
  sameLocation(Posn)      -- boolean
  getX()      -- int
  getY(     -- int
  Methods of Fields:
  this.color.darker()     -- Color
  this.color.brighter()     -- Color

   */

  // Draw this button dark
  WorldImage drawDark() {
    return this.drawButton(this.color.darker().darker());
  }
  /* Methods of Fields:
  this.color.darker()     -- Color
   */

  // Draw this button lit
  WorldImage drawLit() {
    return this.drawButton(this.color.brighter().brighter().brighter());
  }
  /* Methods of Fields:
  this.color.brighter()     -- Color
   */

  // Draw this button
  WorldImage drawButton(Color color) {
    return new RectangleImage(500, 500, OutlineMode.SOLID, color);
  }
  /* Methods of Fields:
  n/a
   */

  // compares whether the two button has the same color
  boolean sameColor(Button button) {
    return button.sameColorHelper(this.color);
  }
  /* Methods of Fields:
  n/a
   */

  // compares two colors
  boolean sameColorHelper(Color color) {
    return this.color == color;
  }
  /* Methods of Fields:
  n/a
   */

  // determines if the given posn is within 500 units in the x and y
  boolean sameLocation(Posn pos) {
    return (pos.x >= this.x - 250) && (pos.x <= this.x + 250) 
        && (pos.y >= this.y - 250) && (pos.y <= this.y + 250);
  }
  /* Methods of Fields:
  n/a
   */

  int getX() {
    return this.x;
  }
  /* Methods of Fields:
  n/a
   */

  int getY() {
    return this.y;
  }
  /* Methods of Fields:
  n/a
   */
}

// Examples
class ExamplesSimon {

  // examples of buttons
  Button greenButton = new Button(Color.GREEN, 250, 250);
  Button greenButton2 = new Button(Color.GREEN, 500, 500);
  Button redButton = new Button(Color.RED, 750, 250);
  Button yellowButton = new Button(Color.YELLOW, 250, 750);
  Button blueButton = new Button(Color.BLUE, 750, 750);

  // examples of lists
  ILoButton randomList = new ConsLoButton(this.greenButton, 
      new ConsLoButton(this.greenButton2, new ConsLoButton(this.redButton, new MtLoButton())));
  ILoButton randomList1 = new ConsLoButton(this.redButton, 
      new ConsLoButton(this.greenButton2, new ConsLoButton(this.greenButton, new MtLoButton())));
  ILoButton randomList2 = new ConsLoButton(this.redButton, 
      new ConsLoButton(this.greenButton2, new ConsLoButton(this.greenButton, 
          new ConsLoButton(this.yellowButton, new MtLoButton()))));
  ILoButton randomList3 =  new ConsLoButton(this.greenButton2, 
      new ConsLoButton(this.greenButton, new MtLoButton()));
  ILoButton mtList = new MtLoButton();
  ILoButton list1 = new ConsLoButton(this.greenButton, new MtLoButton());

  // the constructor
  ExamplesSimon() {}

  // runs the game by creating a world and calling bigBang
  boolean testSimonSays(Tester t) {
    SimonWorld starterWorld = new SimonWorld();
    int sceneSize = starterWorld.sceneSize;
    return starterWorld.bigBang(sceneSize, sceneSize, 0.6);
  }

  // tests a random seed for SimonWorld, which returns yellow buttons
  boolean testSimonSaysTest(Tester t) {
    return t.checkExpect(new SimonWorld(new Random(0)), 
        new SimonWorld(1000, new Random(0),
        new Button(Color.GREEN, 250, 250), new Button(Color.RED, 750, 250),
        new Button(Color.YELLOW, 250, 750), new Button(Color.BLUE, 750, 750),
        new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
        new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
        true, false, false))
        && t.checkExpect(new SimonWorld(new Random(1)), new SimonWorld(1000, new Random(0),
            new Button(Color.GREEN, 250, 250), new Button(Color.RED, 750, 250),
            new Button(Color.YELLOW, 250, 750), new Button(Color.BLUE, 750, 750),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            true, false, false))
        && t.checkExpect(new SimonWorld(new Random(2)), new SimonWorld(1000, new Random(0),
            new Button(Color.GREEN, 250, 250), new Button(Color.RED, 750, 250),
            new Button(Color.YELLOW, 250, 750), new Button(Color.BLUE, 750, 750),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            true, false, false))
        && t.checkExpect(new SimonWorld(new Random(4)), new SimonWorld(1000, new Random(0),
            new Button(Color.GREEN, 250, 250), new Button(Color.RED, 750, 250),
            new Button(Color.YELLOW, 250, 750), new Button(Color.BLUE, 750, 750),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            new ConsLoButton(new Button(Color.YELLOW, 250, 750), new MtLoButton()),
            true, false, false));
  }

  // tests for methods

  /* can't test this because of random value
  boolean testIntToButton(Tester t) {
    return t.checkExpect(new SimonWorld().intToButton(0), this.greenButton)
        && t.checkExpect(new SimonWorld().intToButton(1), this.redButton)
        && t.checkExpect(new SimonWorld().intToButton(3), this.yellowButton)
        && t.checkExpect(new SimonWorld().intToButton(4), this.blueButton);
  }
   */

  boolean testSameLists(Tester t) {
    return t.checkExpect(this.randomList.sameLists(this.greenButton), true)
        && t.checkExpect(this.randomList.sameLists(this.redButton), false)
        && t.checkExpect(this.mtList.sameLists(this.greenButton), false);
  }

  boolean testAddToEnd(Tester t) {
    return t.checkExpect(this.randomList1.addToEnd(this.yellowButton), 
        this.randomList2)
        && t.checkExpect(this.mtList.addToEnd(this.greenButton), this.list1);
  }

  boolean testLength(Tester t) {
    return t.checkExpect(this.randomList1.length(), 3)
        && t.checkExpect(this.mtList.length(), 0);
  }

  boolean testLastButton(Tester t) {
    return t.checkExpect(this.randomList.lastButton(this.redButton), 
        this.greenButton)
        && t.checkExpect(this.mtList.lastButton(this.redButton), this.redButton);
  }

  boolean testRemoveLight(Tester t) {
    return t.checkExpect(this.randomList1.removeLight(), this.randomList3)
        && t.checkExpect(this.mtList.removeLight(), new MtLoButton());
  }

  boolean testRegenerateList(Tester t) {
    return t.checkExpect(this.randomList.regenerateList(this.list1), 
        this.randomList)
        && t.checkExpect(this.mtList.regenerateList(this.list1), this.list1);
  }

  boolean testDrawFirst(Tester t) {
    return t.checkExpect(this.randomList.drawFirst(), 
        this.greenButton.drawLit())
        && t.checkExpect(this.mtList.drawFirst(), new EmptyImage());
  }

  boolean testFirstX(Tester t) {
    return t.checkExpect(this.randomList.firstX(), 250)
        && t.checkExpect(this.mtList.firstX(), 0);
  }

  boolean testFirstY(Tester t) {
    return t.checkExpect(this.randomList.firstY(), 250)
        && t.checkExpect(this.mtList.firstY(), 0);
  }

  boolean testDrawDark(Tester t) {
    return t.checkExpect(this.greenButton.drawDark(), 
        new RectangleImage(500, 500, OutlineMode.SOLID, Color.green.darker().darker()))
        && t.checkExpect(this.redButton.drawDark(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.red.darker().darker()))
        && t.checkExpect(this.yellowButton.drawDark(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.yellow.darker().darker()))
        && t.checkExpect(this.blueButton.drawDark(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.blue.darker().darker()));
  }

  boolean testDrawLit(Tester t) {
    return t.checkExpect(this.greenButton.drawLit(), 
        new RectangleImage(500, 500, OutlineMode.SOLID, Color.green.brighter().brighter()))
        && t.checkExpect(this.redButton.drawLit(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.red.brighter().brighter()))
        && t.checkExpect(this.yellowButton.drawLit(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.yellow.brighter().brighter()))
        && t.checkExpect(this.blueButton.drawLit(), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.blue.brighter().brighter()));
  }

  boolean testDrawButton(Tester t) {
    return t.checkExpect(this.greenButton.drawButton(Color.green), 
        new RectangleImage(500, 500, OutlineMode.SOLID, Color.green))
        && t.checkExpect(this.redButton.drawButton(Color.RED), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.red))
        && t.checkExpect(this.yellowButton.drawButton(Color.YELLOW), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.yellow))
        && t.checkExpect(this.blueButton.drawButton(Color.BLUE), 
            new RectangleImage(500, 500, OutlineMode.SOLID, Color.blue));
  }

  boolean testSameColor(Tester t) {
    return t.checkExpect(this.greenButton.sameColor(greenButton), true)
        && t.checkExpect(this.greenButton.sameColor(greenButton2), true)
        && t.checkExpect(this.greenButton.sameColor(redButton), false);
  }

  boolean testSameColorHelper(Tester t) {
    return t.checkExpect(this.greenButton.sameColorHelper(Color.green), true)
        && t.checkExpect(this.greenButton.sameColorHelper(Color.blue), false);
  }

  boolean testSameLocation(Tester t) {
    return t.checkExpect(this.greenButton.sameLocation(new Posn(0, 0)), true)
        && t.checkExpect(this.greenButton.sameLocation(new Posn(500, 0)), true)
        && t.checkExpect(this.greenButton.sameLocation(new Posn(0, 500)), true)
        && t.checkExpect(this.greenButton.sameLocation(new Posn(500, 500)), true)
        && t.checkExpect(this.greenButton.sameLocation(new Posn(-500, 500)), false)
        && t.checkExpect(this.greenButton.sameLocation(new Posn(500, -500)), false);
  }
}