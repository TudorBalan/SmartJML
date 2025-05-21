package types;

public class Address {

    //@ public invariant (\forall Address a; a.address == this.address; a == this);
    //@ public invariant (\forall Address a; a.address >= 0);
    public final int address;

    //@ public normal_behavior
    //@ ensures this.address = address;
    public Address(int address) {
        this.address = address;
    }

}
