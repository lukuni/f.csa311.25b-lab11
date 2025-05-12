package game;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game; // The current state of the game

    /**
     * Start the server at :8080 port.
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        this.game = new Game(); // Initialize a new game on startup

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:3000/");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

        System.out.println("Request received: " + uri); // Debugging line

        try {
            if (uri.equals("/newgame")) {
                this.game = new Game(); // Create a new game instance
            } else if (uri.equals("/play")) {
                // e.g., /play?x=1&y=1
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y"));
                this.game = this.game.play(x, y); // Call play method on the current game object
            }
            // --- New Undo handling ---
            else if (uri.equals("/undo")) {
                 this.game = this.game.undo(); // Call the new undo method on the game object
            }
            // --- End of New Undo handling ---
            else {
                 // Handle unknown URIs, perhaps return a 404
                 return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
            }

            // Extract the view-specific data from the game and apply it to the template.
            // Return GameState as JSON
            GameState gameplay = GameState.forGame(this.game);
            // Use newFixedLengthResponse with correct content type for JSON
            return newFixedLengthResponse(Response.Status.OK, "application/json", gameplay.toString());

        } catch (NumberFormatException e) {
             // Handle cases where x or y are not valid integers
             return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "Invalid coordinates");
        } catch (Exception e) {
             // Catch any other exceptions during request processing
             e.printStackTrace();
             return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Internal Server Error: " + e.getMessage());
        }
    }

    // The Test class does not seem relevant to the game logic or server,
    // so it can likely be removed or ignored for this assignment.
    // public static class Test {
    //     public String getText() {
    //         return "Hello World!";
    //     }
    // }
}