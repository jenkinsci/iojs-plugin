package jenkins.plugins.iojs.tools;

/**
 * @author fcamblor
 */
public class IojsVersion implements Comparable<IojsVersion> {
    private Integer major;
    private Integer minor;
    private Integer patch;

    public IojsVersion(String version){
        String[] chunkedVersions = version.split("\\.");
        this.major = Integer.valueOf(chunkedVersions[0]);
        this.minor = Integer.valueOf(chunkedVersions[1]);
        this.patch = Integer.valueOf(chunkedVersions[2]);
    }

    public int compareTo(IojsVersion v) {
        int cmp = major.compareTo(v.major);
         if(cmp == 0){
           cmp = minor.compareTo(v.minor);
           if(cmp == 0){
             return patch.compareTo(v.patch);
           }
         }
         return cmp;
    }

    public boolean isLowerThan(IojsVersion version){
        return compareTo(version) < 0;
    }

    public boolean isLowerThan(String version){
        return isLowerThan(new IojsVersion(version));
    }

    public static int compare(String first, String second){
        return new IojsVersion(first).compareTo(new IojsVersion(second));
    }
}
