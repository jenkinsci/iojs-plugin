package jenkins.plugins.iojs.tools;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import hudson.tools.DownloadFromUrlInstaller;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author fcamblor
 */
@RunWith(Parameterized.class)
public class InstallerPathResolversTest {

    private static final IojsInstaller.Platform[] TESTABLE_PLATFORMS = new IojsInstaller.Platform[]{ IojsInstaller.Platform.LINUX, IojsInstaller.Platform.MAC };
    private static final IojsInstaller.CPU[] TESTABLE_CPUS = IojsInstaller.CPU.values();

    private DownloadFromUrlInstaller.Installable installable;
    private final IojsInstaller.Platform platform;
    private final IojsInstaller.CPU cpu;

    public InstallerPathResolversTest(DownloadFromUrlInstaller.Installable installable, IojsInstaller.Platform platform, IojsInstaller.CPU cpu, String testName) {
        this.installable = installable;
        this.platform = platform;
        this.cpu = cpu;
    }

    @Parameterized.Parameters(name = "{index}: {3}")
    public static Collection<Object[]> data() throws IOException {
        Collection<Object[]> testPossibleParams = new ArrayList<Object[]>();

        String installablesJSONStr = Resources.toString(Resources.getResource("updates/hudson.plugins.iojs.tools.IojsInstaller.json"), Charsets.UTF_8);
        JSONArray installables = JSONObject.fromObject(installablesJSONStr).getJSONArray("releases");
        for(int i=0; i<installables.size(); i++){
            DownloadFromUrlInstaller.Installable installable = (DownloadFromUrlInstaller.Installable)installables.getJSONObject(i).toBean(DownloadFromUrlInstaller.Installable.class);

            for(IojsInstaller.Platform platform :TESTABLE_PLATFORMS){
                for(IojsInstaller.CPU cpu :TESTABLE_CPUS){
                    testPossibleParams.add(new Object[]{ installable, platform, cpu, String.format("version=%s,cpu=%s,platform=%s",installable.id,cpu.name(),platform.name()) });
                }
            }
        }
        return testPossibleParams;
    }

    @Test
    public void shouldIojsInstallerResolvedPathExist() throws IOException {
        InstallerPathResolver installerPathResolver = InstallerPathResolver.Factory.findResolverFor(this.installable);
        String path = installerPathResolver.resolvePathFor(installable.id, this.platform, this.cpu);
        URL url = new URL(installable.url+path);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(2000);
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            assertThat(code >= 200 && code < 400, is(true));
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }
}
