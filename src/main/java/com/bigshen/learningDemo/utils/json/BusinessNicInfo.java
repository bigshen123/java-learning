package kl.gw.cloud.pps.model.sso;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author byj
 * @date 2023/8/14
 */
@Getter
@Setter
@NoArgsConstructor
public class BusinessNicInfo {
    private ClusterInterfaceIpAddressConf v4;
    private Integer nicType;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ClusterInterfaceIpAddressConf {
        private boolean enabled;
        private IpAddress cluster;
        private IpAddress primary;
        private IpAddress secondary;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class IpAddress {
        private String ip;
        private String netmask;
    }
}

