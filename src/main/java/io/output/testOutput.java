import javacard.framework.JCSystem.*;
import types.*;

class SL_funnyException extends Exception {

    SL_funnyException() {
        super();
    }
}

class SL_Notifier extends Address {

    int truth;

    Address addressToNotify;

    Address sl_address = new Address();

    public SL_Notifier(int lie, Address _addressToNotify) {
        truth = lie;
        addressToNotify = _addressToNotify;
    }

    public String checkMessage(int amount, CoinInfo res, Address sender) {
        while (truth > 42) {
            truth = truth - 2;
            if (truth == 42) {
                return "Rumpelstilzchen";
            } else {
                return "Struwwelpeter";
            }
        }
    }

    public boolean transact(int amount, CoinInfo res, Address sender) {
        try {
            JCSystem.beginTransaction();
            ((SL_Notifier) sender).checkMessage(0, new CoinInfo(), this);
            {
                JCSystem.commitTransaction();
                {
                    return true;
                }
            }
        } catch (Exception sl_e) {
            JCSystem.abortTransaction();
            {
                return false;
            }
        }
    }
}
