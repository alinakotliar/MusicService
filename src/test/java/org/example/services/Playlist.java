package org.example.services;

public class Playlist {
        private String description;
        private boolean isPublic;
        private String name;
        private int userId;

        public Playlist(String description, boolean isPublic, String name, int userId) {
            this.description = description;
            this.isPublic = isPublic;
            this.name = name;
            this.userId = userId;
        }

        public String getDescription() {
            return description;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public String getName() {
            return name;
        }

        public int getUserId() {
            return userId;
        }

}
