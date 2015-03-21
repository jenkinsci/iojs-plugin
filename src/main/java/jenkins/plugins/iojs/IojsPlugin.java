package jenkins.plugins.iojs;

import hudson.Plugin;
import hudson.model.Hudson;
import jenkins.plugins.iojs.tools.IojsInstallation;

import java.io.IOException;

/**
 * @author fcamblor
 */
public class IojsPlugin extends Plugin {

    IojsInstallation[] installations;

    public IojsPlugin(){
        super();
    }

    @Override
   	public void start() throws Exception {
   		super.start();

   		this.load();

   		// If installations have not been read in iojs.xml, let's initialize them
   		if(this.installations == null){
            this.installations = new IojsInstallation[0];
   		}
   	}

    public IojsInstallation[] getInstallations() {
        return installations;
    }

    public IojsInstallation findInstallationByName(String name) {
        for(IojsInstallation iojsInstallation : getInstallations()){
            if(name.equals(iojsInstallation.getName())){
                return iojsInstallation;
            }
        }
        throw new IllegalArgumentException("io.js installation not found: "+name);
    }

    public void setInstallations(IojsInstallation[] installations) {
        this.installations = installations;
        try {
            this.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IojsPlugin instance() {
        return Hudson.getInstance().getPlugin(IojsPlugin.class);
    }
}
