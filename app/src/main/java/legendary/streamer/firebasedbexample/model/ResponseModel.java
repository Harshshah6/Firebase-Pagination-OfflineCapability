package legendary.streamer.firebasedbexample.model;

public class ResponseModel {
    private String name;
    private String bio;
    private String language;
    private String id;
    private float version;

    public ResponseModel() {
    }

    public ResponseModel(String name, String bio, String language, String id, float version) {
        this.name = name;
        this.bio = bio;
        this.language = language;
        this.id = id;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getLanguage() {
        return language;
    }

    public String getId() {
        return id;
    }

    public Object getVersion() {
        return version;
    }
}
