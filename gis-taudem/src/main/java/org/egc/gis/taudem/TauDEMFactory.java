package org.egc.gis.taudem;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/25 16:26
 */
public abstract class TauDEMFactory {

    private BasicGridAnalysis basicGridAnalysis;
    private SpecializedGridAnalysis specializedGridAnalysis;
    private StreamNetworkAnalysis streamNetworkAnalysis;
    private SINMAPStabilityIndex sinmapStabilityIndex;


    public BasicGridAnalysis getBasicGridAnalysis() {
        return basicGridAnalysis;
    }
}
