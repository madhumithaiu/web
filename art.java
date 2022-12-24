
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Interface for an art gallery management system
interface ArtGallery {
    void addArtwork(Artwork artwork);

    void removeArtwork(Artwork artwork);

    void displayArtwork();
}

// Abstract class for an artwork in the art gallery
abstract class Artwork {
    String artist;
    String title;
    String medium;
    int year;
    double price;

    public Artwork(String artist, String title, String medium, int year, double price) {
        this.artist = artist;
        this.title = title;
        this.medium = medium;
        this.year = year;
        this.price = price;
    }
}

// Concrete class for a painting in the art gallery
class Painting extends Artwork {
    String style;

    public Painting(String artist, String title, String medium, int year, double price, String style) {
        super(artist, title, medium, year, price);
        this.style = style;
    }
}

// Concrete class for a sculpture in the art gallery
class Sculpture extends Artwork {
    String material;

    public Sculpture(String artist, String title, String medium, int year, double price, String material) {
        super(artist, title, medium, year, price);
        this.material = material;
    }
}

// Event handler for the art gallery management system
class ArtGalleryEventHandler {
    ArtGallery artGallery;

    public ArtGalleryEventHandler(ArtGallery artGallery) {
        this.artGallery = artGallery;
    }

    public void handleAddArtworkEvent(Artwork artwork) {
        artGallery.addArtwork(artwork);
    }

    public void handleRemoveArtworkEvent(Artwork artwork) {
        artGallery.removeArtwork(artwork);
    }

    public void handleDisplayArtworkEvent() {
        artGallery.displayArtwork();
    }
}

// Thread for handling events in the art gallery management system
class EventHandlerThread extends Thread {
    ArtGalleryEventHandler eventHandler;
    Artwork artwork;
    String event;

    public EventHandlerThread(ArtGalleryEventHandler eventHandler, String event, Artwork artwork) {
        this.eventHandler = eventHandler;
        this.event = event;
        this.artwork = artwork;
    }

    @Override
    public void run() {
        switch (event) {
            case "add":
                eventHandler.handleAddArtworkEvent(artwork);
                break;
            case "remove":
                eventHandler.handleRemoveArtworkEvent(artwork);
                break;
            case "display":
                eventHandler.handleDisplayArtworkEvent();
                break;
            default:
                System.out.println("Invalid event");
        }
    }
}

// Concrete class for the art gallery management system
class ArtGalleryManagementSystem implements ArtGallery {
    List<Artwork> artworks = new ArrayList<>();

    @Override
    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }

    @Override
    public void removeArtwork(Artwork artwork) {
        for (int i = 0; i < artworks.size(); i++) {
            if (artworks.get(i).artist.equals(artwork.artist) && artworks.get(i).title.equals(artwork.title)) {
                artworks.remove(artworks.get(i));
                break;
            }
        }
        System.out.println("Artwork removed");
    }

    @Override
    public void displayArtwork() {
        for (Artwork artwork : artworks) {
            System.out.println("Artist: " + artwork.artist);
            System.out.println("Title: " + artwork.title);
            System.out.println("Medium: " + artwork.medium);
            System.out.println("Year: " + artwork.year);
            System.out.println("Price: " + artwork.price);
            if (artwork instanceof Painting) {
                System.out.println("Style: " + ((Painting) artwork).style);
            } else if (artwork instanceof Sculpture) {
                System.out.println("Material: " + ((Sculpture) artwork).material);
            }
        }
    }

    public static void main(String[] args) {
        ArtGallery artGallery = new ArtGalleryManagementSystem();
        ArtGalleryEventHandler eventHandler = new ArtGalleryEventHandler(artGallery);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter event (add, remove, display, exit): ");
            String event = scanner.nextLine();
            if (event.equals("add")) {
                System.out.println("Enter artwork type (painting, sculpture): ");
                String type = scanner.nextLine();
                System.out.println("Enter artist name: ");
                String artist = scanner.nextLine();
                System.out.println("Enter title: ");
                String title = scanner.nextLine();
                System.out.println("Enter medium: ");
                String medium = scanner.nextLine();
                System.out.println("Enter year: ");
                int year = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter price: ");
                double price = scanner.nextDouble();
                scanner.nextLine();
                Artwork artwork = null;
                if (type.equals("painting")) {
                    System.out.println("Enter style: ");
                    String style = scanner.nextLine();
                    artwork = new Painting(artist, title, medium, year, price, style);
                } else if (type.equals("sculpture")) {
                    System.out.println("Enter material: ");
                    String material = scanner.nextLine();
                    artwork = new Sculpture(artist, title, medium, year, price, material);
                }
                EventHandlerThread eventThread = new EventHandlerThread(eventHandler, event, artwork);
                eventThread.start();
            } else if (event.equals("remove")) {
                System.out.println("Enter artist name: ");
                String artist = scanner.nextLine();
                System.out.println("Enter title: ");
                String title = scanner.nextLine();
                Artwork artwork = new Painting(artist, title, "", 0, 0, "");
                EventHandlerThread eventThread = new EventHandlerThread(eventHandler, event, artwork);
                eventThread.start();
            } else if (event.equals("display")) {
                EventHandlerThread eventThread = new EventHandlerThread(eventHandler, event, null);
                eventThread.start();
            } else if (event.equals("exit")) {
                break;
            } else {
                System.out.println("Invalid event");
            }
        }
    }
}
