package data;

public class NewPet {
    private int id;
    private String name;
    private String[] photoUrls;

    public NewPet(int id, String name, String[] photoUrls) {
        this.id = id;
        this.name = name;
        this.photoUrls = photoUrls;
    }

    public NewPet() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getPhotoUrls() {
        return photoUrls;
    }
}

