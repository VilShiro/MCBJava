package org.fbs.mcb.data.client;

import org.fbs.mcb.annotation.UserMapping;

public class UserMapper {

    private final UserManager manager;

    public UserMapper(UserMapping mapper) {
        manager = map(mapper);
    }

    private UserManager map(UserMapping mapping) {
        if (mapping == null){
            throw new IllegalArgumentException("UserMapping is required");
        }

        BotAction add = null;
        BotAction end = null;
        BotAction timeout = null;

        // Add strategy mapping
        switch (mapping.onAdd()) {
            case INTERRUPT -> {
                add = new BotAction(){
                    @Override
                    public void invoke(Object... args) {
                        assert args[0] instanceof BotUser;
                        assert args[1] instanceof Runnable;

                        BotUser user = (BotUser) args[0];
                        Runnable action = (Runnable) args[1];



                    }
                };
                break;
            }
            case STACK_ADDITION -> {
                break;
            }
            case IGNORING_WHILE_IN_PROGRESS -> {
                break;
            }
        }

        BotAction finalAdd = add;
        return new UserManager(){
            @Override
            public void add(Object... args) {
                finalAdd.invoke(args);
            }

            @Override
            public void end() {
                end.invoke();
            }

            @Override
            public void timeout() {
                timeout.invoke();
            }
        };
    }

    private static abstract class BotAction {

        public abstract void invoke(Object... args);

    }

}
