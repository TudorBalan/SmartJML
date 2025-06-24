exception funnyException(string s)

contract Notifier {
    int truth;
    address addressToNotify;

    constructor(int lie, address _addressToNotify){
        truth = lie;
        addressToNotify = _addressToNotify;
    }

    checkMessage() returns string {
        while (truth > 42) {
            truth = truth - 2;
            if (truth == 42) {
                return 'Rumpelstilzchen';
            } else {
                return "Struwwelpeter";
            }
        }
    }

    transact() returns bool {
        try
            sender.checkMessage();
        abort {
            return false;
        } success {
            return true;
        }
    }
}