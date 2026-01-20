package io.github.krmdemo.kafkainspect.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.randomuser.RandomUser;

public interface RandomUsersClient {

    RandomUser getRandomUser();

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
