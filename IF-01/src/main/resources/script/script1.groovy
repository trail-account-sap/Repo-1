import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

def Message processData(Message message) {


    def props = message.getProperties()

    String idoctyp = props.get("IDOCTYP")
    String cimtyp  = props.get("CIMTYP")
    String mestyp  = props.get("MESTYP")
    String receiver = props.get("RCVPRN")


    String idocIdentifier
    if (cimtyp != null && !cimtyp.trim().isEmpty()) {
        idocIdentifier = receiver + "-" + mestyp + "." + idoctyp + "."+cimtyp
    } else {
        idocIdentifier = receiver + "-" + mestyp + "." + idoctyp
    }


    //String pdIdentifier = idocIdentifier + "_" + receiver


    ValueMappingApi api = ITApiFactory.getApi(ValueMappingApi.class, null)
    if (api == null) {
        throw new Exception("Could not retrieve ValueMappingApi")
    }


    String iflowIdentifier = api.getMappedValue("SAP","IdocIdentifier",idocIdentifier,"SAPBTP","IflowIdentifier")

    if (iflowIdentifier == null || iflowIdentifier.trim().isEmpty()) {
        throw new Exception("No value mapping found for key: " + idocIdentifier)
    }

    message.setHeader("PD_Address", iflowIdentifier)

    return message
}