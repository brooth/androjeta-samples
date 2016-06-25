package org.brooth.androjeta.samples.databinding;

import org.brooth.jeta.MasterController;
import org.brooth.jeta.metasitory.Metasitory;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class DataBindController<M>  extends MasterController<M, DataBindMetacode<M>> {

    public DataBindController(Metasitory metasitory, M master) {
        super(metasitory, master, DataBind.class);
    }

    public void apply() {
        for(DataBindMetacode<M> metacode : metacodes)
            metacode.apply(master);
    }
}
