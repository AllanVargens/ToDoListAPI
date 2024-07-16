package com.vargens.api_todo_list.controller.dto.User;

import java.util.Optional;

public record UpdateUserDTO(Optional<String> username, Optional<String> email, Optional<String> password) {

    public static class Builder {
        private Optional<String> username;
        private Optional<String> email;
        private Optional<String> password;

        public Builder() {
        }

        public Builder withUsername(String username) {
            this.username = Optional.ofNullable(username);
            return this;
        }

        public Builder withEmail(String email) {
            this.email = Optional.ofNullable(email);
            return this;
        }

        public Builder withPassword(String password) {
            this.password = Optional.ofNullable(password);
            return this;
        }

        public UpdateUserDTO build() {
            return new UpdateUserDTO(username, email, password);
        }
    }
}
