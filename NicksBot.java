package bots;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

import java.awt.*;

public class NicksBot extends Bot {
    /**
     * Bot Strategy:
     *
     * The strategy for this bot is as follows:
     *
     * Step 1. Check if there are any bullets too close to this bot. If there are any, then this bot moves according to the
     * location of this bot compared to the bullet. If there are no bullets too close, proceed to Step 2.
     *
     * Step 2. Check if the this bot is in firing distance. If true, shoot in the direction according to the comparison between
     * the closest bot's location and this bot's location and the x and y displacements. If this bot is not within firing distance,
     * proceed to Step 3.
     *
     * Step 3. Finally, I check if this bot should move along the x or y axis. Then, when the bot has decided which
     * axis to move on, it will compare it's location with the closest bot's location and move closer to it.
     *
     */
    private BotHelper helperBot = new BotHelper(); // To access methods from the BotHelper class
    private static double FIREDISTANCE = 200; // Distance at which the bot should fire
    private static double AVOID = 50; // Distance at which the bot should be avoiding
    private Bullet closestBullet; // Holds the closest bullet
    private BotInfo closestBot; // Holds the closest bot
    /**
     * This method is called at the beginning of each round. Use it to perform
     * any initialization that you require when starting a new round.
     */
    @Override
    public void newRound() {

    }

    /**
     * This method is called at every time step to find out what you want your
     * Bot to do. The legal moves are defined in constants in the BattleBotArena
     * class (UP, DOWN, LEFT, RIGHT, FIREUP, FIREDOWN, FIRELEFT, FIRERIGHT, STAY,
     * SEND_MESSAGE). <br><br>
     * <p>
     * The <b>FIRE</b> moves cause a bullet to be created (if there are
     * not too many of your bullets on the screen at the moment). Each bullet
     * moves at speed set by the BULLET_SPEED constant in BattleBotArena. <br><br>
     * <p>
     * The <b>UP</b>, <b>DOWN</b>, <b>LEFT</b>, and <b>RIGHT</b> moves cause the
     * bot to move BOT_SPEED
     * pixels in the requested direction (BOT_SPEED is a constant in
     * BattleBotArena). However, if this would cause a
     * collision with any live or dead bot, or would move the Bot outside the
     * playing area defined by TOP_EDGE, BOTTOM_EDGE, LEFT_EDGE, and RIGHT_EDGE,
     * the move will not be allowed by the Arena.<br><Br>
     * <p>
     * The <b>SEND_MESSAGE</b> move (if allowed by the Arena) will cause a call-back
     * to this Bot's <i>outgoingMessage()</i> method, which should return the message
     * you want the Bot to broadcast. This will be followed with a call to
     * <i>incomingMessage(String)</i> which will be the echo of the broadcast message
     * coming back to the Bot.
     *
     * @param me       A BotInfo object with all publicly available info about this Bot
     * @param shotOK   True iff a FIRE move is currently allowed
     * @param liveBots An array of BotInfo objects for the other Bots currently in play
     * @param deadBots An array of BotInfo objects for the dead Bots littering the arena
     * @param bullets  An array of all Bullet objects currently in play
     * @return A legal move (use the constants defined in BattleBotArena)
     */
    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        closestBot = helperBot.findClosest(me, liveBots); // Get the closest bot

        if (bullets.length > 0) { // Check if there are any bullets
            closestBullet = helperBot.findClosest(me, bullets); // Get the closest bullet
        }
        if (helperBot.calcDistance(me.getX(), me.getY(), closestBullet.getX(), closestBullet.getY()) < AVOID) { // Check if the distance between this bot and the bullet is too close
            if (closestBullet.getXSpeed() < 0) { // Check if the bullet is moving left
                if (me.getY() < closestBullet.getY()) { // Check if my bot is higher on the y-axis
                    return BattleBotArena.UP;
                } else {
                    return BattleBotArena.DOWN;
                }
            } else if (closestBullet.getXSpeed() > 0) { // Check if the bullet is moving right
                if (me.getY() < closestBullet.getY()) { // Check if my bot is higher on the y-axis
                    return BattleBotArena.UP;
                } else {
                    return BattleBotArena.DOWN;
                }
            } else if (closestBullet.getYSpeed() < 0) { // Check if the bullet is moving up
                if (me.getX() < closestBullet.getX()) { // Check if my bot is lower on the x-axis
                    return BattleBotArena.LEFT;
                } else {
                    return BattleBotArena.RIGHT;
                }
            } else if (closestBullet.getYSpeed() > 0) { // Check if the bullet is moving down
                if (me.getX() < closestBullet.getX()) { // Check if my bot is lower on the x-axis
                    return BattleBotArena.LEFT;
                } else {
                    return BattleBotArena.RIGHT;
                }
            }
        } else if (BotHelper.manhattanDist(me.getX(), me.getY(), closestBot.getX(), closestBot.getY()) <= FIREDISTANCE) { // Check if this bot is within firing range
                if (helperBot.calcDisplacement(me.getX(), closestBot.getX()) > helperBot.calcDisplacement(me.getY(), closestBot.getY())) { // Decide where to fire
                    if (me.getY() < closestBot.getY()) { // Check if this bot's location is higher than the closest bot's location
                        return BattleBotArena.FIREDOWN;
                    } else if (me.getY() > closestBot.getY()) { // Check if this bot's location is lower than the closest bot's location
                        return BattleBotArena.FIREUP;
                    }

                } else {
                    if (me.getX() < closestBot.getX()) { // Check if this bot's location is to the left of the closest bot location
                        return BattleBotArena.FIRERIGHT;
                    } else if (me.getX() > closestBot.getX()) { // Check if this bot's location is to the right of the closest bot location
                        return BattleBotArena.FIRELEFT;
                    }
                }
            } else if (helperBot.calcDisplacement(me.getX(), closestBot.getX()) > helperBot.calcDisplacement(me.getY(), closestBot.getY())) { // Check if the x displacement is greater than the y displacement
                if (closestBot.getY() < me.getY()) {  // Check if the closest bot is above
                        return BattleBotArena.UP;
                } else if (closestBot.getX() > me.getX()) { // Check if the closest bot is to the right
                    return BattleBotArena.RIGHT;
                }
            } else {
                if (closestBot.getX() < me.getX()) { // Check if the closest bot is to the left
                        return BattleBotArena.LEFT;
                } else if (closestBot.getY() > me.getY()) {  // Check if the closest bot is below
                    return BattleBotArena.DOWN;
                }
            }
        return 0; // Return nothing
    }



    /**
     * Called when it is time to draw the Bot. Your Bot should be (mostly)
     * within a circle inscribed inside a square with top left coordinates
     * <i>(x,y)</i> and a size of <i>RADIUS * 2</i>. If you are using an image,
     * just put <i>null</i> for the ImageObserver - the arena has some special features
     * to make sure your images are loaded before you will use them.
     *
     * @param g The Graphics object to draw yourself on.
     * @param x The x location of the top left corner of the drawing area
     * @param y The y location of the top left corner of the drawing area
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.fillRect(x+2, y+2, RADIUS*2-4, RADIUS*2-4);
    }

    /**
     * This method will only be called once, just after your Bot is created,
     * to set your name permanently for the entire match.
     *
     * @return The Bot's name
     */
    @Override
    public String getName() {
        return "Nick's Bot";
    }

    /**
     * This method is called at every time step to find out what team you are
     * currently on. Of course, there can only be one winner, but you can
     * declare and change team allegiances throughout the match if you think
     * anybody will care. Perhaps you can send coded broadcast message or
     * invitation to other Bots to set up a temporary team...
     *
     * @return The Bot's current team name
     */
    @Override
    public String getTeamName() {
        return null;
    }

    /**
     * This is only called after you have requested a SEND_MESSAGE move (see
     * the documentation for <i>getMove()</i>). However if you are already over
     * your messaging cap, this method will not be called. Messages longer than
     * 200 characters will be truncated by the arena before being broadcast, and
     * messages will be further truncated to fit on the message area of the screen.
     *
     * @return The message you want to broadcast
     */
    @Override
    public String outgoingMessage() {
        return null;
    }

    /**
     * This is called whenever the referee or a Bot sends a broadcast message.
     *
     * @param botNum The ID of the Bot who sent the message, or <i>BattleBotArena.SYSTEM_MSG</i> if the message is from the referee.
     * @param msg    The text of the message that was broadcast.
     */
    @Override
    public void incomingMessage(int botNum, String msg) {

    }

    /**
     * This is called by the arena at startup to find out what image names you
     * want it to load for you. All images must be stored in the <i>images</i>
     * folder of the project, but you only have to return their names (not
     * their paths).<br><br>
     * <p>
     * PLEASE resize your images in an image manipulation
     * program. They should be squares of size RADIUS * 2 so that they don't
     * take up much memory.
     *
     * @return An array of image names you want the arena to load.
     */
    @Override
    public String[] imageNames() {
        return new String[0];
    }

    /**
     * Once the arena has loaded your images (see <i>imageNames()</i>), it
     * calls this method to pass you the images it has loaded for you. Store
     * them and use them in your draw method.<br><br>
     * <p>
     * PLEASE resize your images in an
     * image manipulation program. They should be squares of size RADIUS * 2 so
     * that they don't take up much memory.<br><br>
     * <p>
     * CAREFUL: If you got the file names wrong, the image array might be null
     * or contain null elements.
     *
     * @param images The array of images (or null if there was a problem)
     */
    @Override
    public void loadedImages(Image[] images) {

    }
}
