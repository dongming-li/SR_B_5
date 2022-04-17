package edu.iastate.a309.darkplatform.display;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.iastate.a309.darkplatform.camera.Camera;
import edu.iastate.a309.darkplatform.entity.Player;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.global.GlobalMethods;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.json.MapDeserializer;
import edu.iastate.a309.darkplatform.json.MapSerializer;
import edu.iastate.a309.darkplatform.map.Map;
import edu.iastate.a309.darkplatform.sockets.PhoneConnection;
import edu.iastate.a309.darkplatform.utility.Vector2f;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import edu.iastate.a309.darkplatform.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * @author Joshua Kuennen
 * <p>
 * This class creates and maintains a JFrame object. It also contains the main game loop which is the basis for
 * updating and drawing the game to the JFrame.
 */
public class Frame extends Canvas implements Runnable {

    /**
     * Title of the game. Will be used as the title for the JFrame.
     */
    private static final String TITLE = "Dark Platform";

    /**
     * Maximum dimensions that the game can be (full screen). This is based on the main screen of the computer the
     * game is running on.
     */
    public static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    /**
     * Current and default dimensions of the game. It is full screen by default.
     */
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;

    /**
     * Number of times that the game will update logic and render to the Canvas.
     */
    private static final int UPDATES_PER_SECOND = 60;

    /**
     * Current version of the game. Can be used to avoid version mismatch on multiplayer.
     */
    public static final String VERSION = "0.0.1";

    /**
     * The instance of the JFrame. Used if some functionality in another class needs information about the frame.
     */
    private static JFrame frame;

    /**
     * Whether or not the game is currently running.
     */
    private boolean running = false;

    /**
     * The instance of the Frame object(this). Used if some functionality in another class needs information about this.
     */
    private static Frame game;

    private Screen screen;
    private InputHandler input;
    public static Camera camera;
    public Player player;

    public Map map;

    public Window window;
//    public List<Entity> localEnts;
//    public List<Entity> remoteEnts;

    public PhoneConnection connection;
//    public Lobby lobby;
//    public Client client;

    private boolean justSwitched;
    /**
     * Default constructor. Class init and sets game to the newly created Frame.
     */
    public Frame() {
        screen = new Screen(width, height);

        init();
        game = this;
    }

    /**
     * Create and set up a JFrame object. Since the current default screen size is full screen, take away the
     * surrounding window gui. Once setup is complete, calls the start method.
     */
    public void init() {


        frame = new JFrame(TITLE + " v" + VERSION);
        frame.setUndecorated(true);
        Dimension dimension = new Dimension(width, height);
        this.setPreferredSize(dimension);

        //TODO: Add code to give the game an icon here

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.add(this);

        input = new InputHandler(this);
        //TODO: Add code for input handlers here

        frame.pack();
        frame.setLocationRelativeTo(null);
        this.requestFocus();
        frame.setVisible(true);

        //TODO: Add code for logging at shutdown here


        //Other class inits
        ArtManager.init();
        PhoneConnection.init();
        GlobalMethods.init();

        justSwitched = false;

//        localEnts = new ArrayList<Entity>();
//        remoteEnts = new ArrayList<Entity>();

        camera = new Camera(new Vector2f(0, 0));
        GlobalFields.map = new Map("map1");
        player = new Player(new Vector2f(800, 450));

        window = new Window(260, 200, new Vector2f(500, 500));
        Window.generateEditWindowStrings(window);
        Window.generateEditWindowSprites(window);

        //localEnts.add(new Entity(new Vector2f(50f,50f)));

//        lobby = new Lobby(20201, "My Lobby");
//        client = new Client("localhost", 20201);
//
//        for(int i = 0; i < 20; i++){
//            lobby.addEnt(new Platform(new Vector2f(300f, 300f)));
//        }

//        connection = new PhoneConnection();
//        new Thread(connection).start();
        this.start();
    }

    /**
     * Main method and entry point for our engine. Only creates a new instance of Frame.
     *
     * @param args arguments passed to the program
     */
    public static void main(String[] args) {
        new Frame();
    }

    /**
     * Starts the game loop by setting running to true and starting a thread based on this. Since Frame implements
     * Runnable, a thread can be made around it.
     */
    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    /**
     * @see Runnable#run()
     * <p>
     * Contains the main game loop.
     */
    public void run() {
        int frames = 0, updates = 0; //frames and updates represent the number of each what the name implies since the last printf statement
        long fpsTimer = System.currentTimeMillis(); //used to keep track of when the last printf statement was
        double nsPerUpdate = 1000000000.0d / UPDATES_PER_SECOND; // number of nano seconds in one update
        double then = System.nanoTime(); // Time of the last check for an update/frame
        double delta = 0; // Time since last update and frame. 1 means that its time to update and render again

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Map.class, new MapSerializer());
        module.addDeserializer(Map.class, new MapDeserializer());
        mapper.registerModule(module);
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(mapper);
        restTemplate.getMessageConverters().add(messageConverter);
        restTemplate.postForObject(GlobalFields.ip + "map", GlobalFields.map, Map.class);


        new updateServer().start();
        //Game Loop
        while (running) {
            double now = System.nanoTime(); // Current time
            delta += (now - then) / nsPerUpdate; // Update delta
            then = now; //set then to now since the last "update/frame" check is now "now"
            boolean canRender = false; // false means only render when there was an update, true means unlimited frames

            while (delta > 1) { // Really is an if statement, but is a while just in case some bad lag happens in order to catch the game logic back up
                updates++;
                update();
                delta -= 1;
                canRender = true;
            }

            if (canRender) {
                frames++;
                render();
                canRender = false;
            }

            // Display frames and updates every second
            if (System.currentTimeMillis() - fpsTimer > 1000) {
                System.out.printf("%d updates, %d frames %n", updates, frames);
                frames = 0;
                updates = 0;
                fpsTimer += 1000;
            }
        }
    }

    /**
     * Everything that needs to be updated game logic wise goes in this method. Gets called updatesPerSecond times
     * per second by the game loop.
     */
    public void update(){
        if(input.keyboard.isKeyPressed(input.keyboard.keyM) && !justSwitched){
            GlobalFields.EDIT_MODE = !GlobalFields.EDIT_MODE;
            justSwitched = true;
        }
        if(!input.keyboard.isKeyPressed(input.keyboard.keyM) && justSwitched){
            justSwitched = false;
        }

        if(GlobalFields.EDIT_MODE){
            camera.update(input);


        } else {
            player.update(input);
        }

        GlobalFields.map.update(input);

        window.update(input);

//        lobby.write();
//
//        DPDatabase clientDatabase = new DPDatabase("To Server");
//        for(Entity e: localEnts){
//            e.update(input);
//            clientDatabase.addObject(e.serialize());
//        }
//        byte[] data = client.getLobbyData();
//
//        DPDatabase lobbyDataBase = DPDatabase.deserialize(data);
//        data = new byte[clientDatabase.getSize()];
//        clientDatabase.getBytes(data, 0);
//        client.sendData(data);
//
//        if(lobbyDataBase != null){
//        remoteEnts.clear();
//        for(DPObject object: lobbyDataBase.objects){
//            if(object.getName().matches("Entity")){
//                remoteEnts.add(Entity.deserialize(object));
//            } else if(object.getName().matches("Platform")){
//                remoteEnts.add(Platform.deserialize(object));
//            }
//        }
//
//        }
//
//        lobby.update(input);
//
//
//        lobby.read();

//        System.out.println("Client to lobby\n");
//        client.sendData();
//        System.out.print(lobby.read());
//
//        System.out.println("Lobby to client\n");
//        lobby.write();
//        System.out.print(client.getLobbyData());

        if (input.keyboard.isKeyPressed(input.keyboard.keyEsc)) {
            stop();
        }
    }

    /**
     * Everything that needs to be rendered to the screen should go in this method. Gets called every time there is an
     * update, unless you have unlimited frames on.
     */
    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        GlobalFields.map.render(screen);

        player.render(screen);

        window.render(screen);


        graphics.drawImage(screen.imageToDraw,0, 0, getWidth(), getHeight(), null);
        screen.clear();

        graphics.setColor(Color.RED);
        graphics.setFont(new Font(graphics.getFont().getName(), graphics.getFont().getStyle(), 20));
        graphics.drawString("Test", 250, 250);

        window.renderWords(graphics);
        graphics.dispose();
        bufferStrategy.show();
    }

    /**
     * Stops the game loop by setting running to false.
     */
    public synchronized void stop() {
        running = false;
        System.exit(0);
    }

    public class updateServer extends Thread {
        private volatile boolean cancelled;
        public void run() {
            while (!cancelled) {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.put(GlobalFields.ip + "map", GlobalFields.map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }
    }

    public static Frame instance() {
        return game;
    }
}
