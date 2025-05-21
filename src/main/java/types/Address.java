package types;

public class Address {

    //@ public invariant (\forall Address a; a.address == this.address; a == this);
    //@ public invariant (\forall Address a; a.address >= 0);
    public final int sl_address;

    public Address(int address) {
        this.sl_address = address;
    }

    public Address(){
        this.sl_address = 0;
    }

}
