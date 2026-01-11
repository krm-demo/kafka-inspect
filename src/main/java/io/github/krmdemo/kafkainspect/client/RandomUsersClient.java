package io.github.krmdemo.kafkainspect.client;

public interface RandomUsersClient {

    static Factory kind(HttpClientKind kind) {
        return null; //new Factory(kind);
    }

    abstract class Factory {
        private final HttpClientKind kind;
        private String baseUrl;
        private Factory(HttpClientKind kind) {
            this.kind = kind;
        }
        public Factory baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        abstract public RandomUsersClient create();
    }

//    class FeignFactory extends Factory {
//        @Override
//        public RandomUsersClient create() {
//            return null;
//        }
//    }
}
