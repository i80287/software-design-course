package hwtask1

fun fillStore(store: StoreRegistry) {
    store.addItemIfNotExists("The Art of Computer Programming [Knuth]", 4);
    store.addItemIfNotExists("Hackers Delight [Warren]", 2);
    store.addItemIfNotExists("Introduction to Algorithms [Cormen]", 2);
    store.addItemIfNotExists("Analysis I [Tao]", 5);
    store.addItemIfNotExists("Analysis II [Tao]", 9);
    store.addItemIfNotExists("The C++ Programming Language [Stroustrup]", 7);
}

fun main() {
    val users = StaffRegistry();
    val store = StoreRegistry();
    val userHandler = UserHandler(users);

    if (!userHandler.loginOrRegisterUser()) {
        userHandler.notifyExit();
        return;
    }

    try {
        while (true) {
            when (userHandler.nextCommand()) {
                UserHandler.UserCommand.ListItems -> userHandler.writeItemsList(store.itemsToString());
                UserHandler.UserCommand.BuyItem -> {
                    val itemName: String = userHandler.getItemName();
                    val itemQuantity: Int = userHandler.getItemQuantity();
                    if (!store.buyItem(itemName, itemQuantity)) {
                        userHandler.notifyBuyingFailure();
                    }
                }
                UserHandler.UserCommand.PayForItems -> {
                    val cartWasNotEmpty: Boolean = store.makePurchase(userHandler.getPayingCard());
                    userHandler.notifyPurchase(cartWasNotEmpty);
                }
                UserHandler.UserCommand.ExitRequested -> {
                    userHandler.notifyExit();
                    break;
                }
            }
        }
    } catch (ex: Exception) {
        userHandler.notifyError(ex);
    } finally {
        store.saveRegistryState();
        users.saveRegistryState();
    }
}
