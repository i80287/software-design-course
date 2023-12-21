package hwtask1

public class UserHandler(private val registry: StaffRegistry) {
    private enum class UserState { ChooseOption, SignIn, SignUp, LoggedIn, ExitRequested };

    public enum class UserCommand { ListItems, BuyItem, PayForItems, ExitRequested };

    private var currentState = UserState.ChooseOption;

    public fun loginOrRegisterUser(): Boolean {
        while (true) {
            when (currentState) {
                UserState.ChooseOption -> currentState = chooseSignInUp();
                UserState.SignIn -> currentState = signInUser();
                UserState.SignUp -> currentState = signUpUser();
                UserState.LoggedIn -> return true;
                UserState.ExitRequested -> return false;
            }
        }
    }

    public fun nextCommand(): UserCommand {
        while (true) {
            print("Write number of the option you want to peek:\n" +
                  "     1. List all awailable goods\n" +
                  "     2. Select goods with the specific name\n" +
                  "     3. Pay for the selected goods\n" +
                  "     4. Exit\n" +
                  "> ");

            val userInput: String? = readLine();
            if (userInput != null && userInput.length == 1) {
                when (userInput[0]) {
                    '1' -> return UserCommand.ListItems;
                    '2' -> return UserCommand.BuyItem;
                    '3' -> return UserCommand.PayForItems;
                    '4' -> return UserCommand.ExitRequested;
                };
            }

            println("Unknown option, expected number 1, 2, 3 or 4. Please, try again");
        }
    }

    public fun writeItemsList(itemsList: String) = println("Items in the store and their quantity:\n" + itemsList);

    public fun getItemName(): String = getNonEmptyInput("Write name of item that you want to buy\n> ");

    public fun getItemQuantity(): Int = getPositiveInt("Write the number of items you want to buy\n> ");

    public fun notifyBuyingFailure() = println("The specified item does not exist or there is not enough of it to purchase");

    public fun getPayingCard() = getPositiveInt("Write payment card number\n> ");

    public fun notifyPurchase(cartWasNotEmpty: Boolean) = println(if (cartWasNotEmpty) "Purchase completed" else "Goods cart is empty");

    public fun notifyExit() = println("Exit requested by user");

    public fun notifyError(ex: Exception) {
        println("Unexpected error occured: $ex");
    }

    private fun chooseSignInUp(): UserState {
        while (true) {
            print("Write number of the option you want to peek:\n" +
                  "     1. Sign in (using existing login)\n" +
                  "     2. Sign up (create new user)\n" +
                  "     3. Exit\n" +
                  "> ");

            val userInput: String? = readLine();
            if (userInput != null && userInput.length == 1) {
                when (userInput[0]) {
                    '1' -> return UserState.SignIn;
                    '2' -> return UserState.SignUp;
                    '3' -> return UserState.ExitRequested;
                };
            }

            println("Number 1 or 2 was expected. Please, try again");
        }
    }

    private fun signInUser(): UserState {
        while (true) {
            print("Enter login or press Enter to return:\n> ");
            val login: String? = readLine();

            if (login.isNullOrEmpty()) {
                return UserState.ChooseOption;
            }

            if (registry.isRegistered(login)) {
                return requestCorrectPassword(login);
            }

            println("User not found. Try again")
        }
    }

    private fun requestCorrectPassword(login: String): UserState {
        while (true) {
            print("Enter password or press Enter to return:\n> ");
            val password: String? = readLine();

            if (password.isNullOrEmpty()) {
                return UserState.ChooseOption;
            }

            if (registry.verifyPassword(login, password)) {
                return UserState.LoggedIn;
            }

            println("Incorrect password. Please, try again")
        }
    }

    private fun signUpUser(): UserState {
        print("Write name for the new user or press Enter to return:\n> ");
        val username: String? = readLine();
        if (username.isNullOrEmpty()) {
            return UserState.ChooseOption;
        }

        if (registry.isRegistered(username)) {
            println("User with this name already exists, redirecting to signing in");
            return signInUser();
        }

        print("Write a new password or press Enter to return:\n> ");
        val password: String? = readLine();
        if (password.isNullOrEmpty()) {
            return UserState.ChooseOption;
        }

        var checkPassword: String?;
        while (true) {
            print("Write a this password again or press Enter to return:\n> ");
            checkPassword = readLine();

            if (checkPassword.isNullOrEmpty()) {
                return UserState.ChooseOption;
            }

            if (password == checkPassword) {
                break;
            }

            print("Passwords do not match. Please, try again");
        }

        registry.registerUser(username, password);
        return UserState.LoggedIn;
    }

    private fun getNonEmptyInput(prompt: String): String {
        while (true) {
            print(prompt);
            val input: String? = readLine();
            if (!input.isNullOrEmpty()) {
                return input;
            }

            println("Empty input. Please, try again");
        }
    }

    private fun getPositiveInt(prompt: String): Int {
        while (true) {
            print(prompt);
            val num: Int? = readLine()?.toIntOrNull();
            if (num != null && num > 0) {
                return num;
            }

            println("Incorrect input, positive integer number was expected. Please, try again");
        }
    }
};
