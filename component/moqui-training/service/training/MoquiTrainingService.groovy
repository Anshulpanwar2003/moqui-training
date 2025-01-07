import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityValue

def createMoquiTraining(Map context) {
    ExecutionContext ec = context.ec  // Retrieve ExecutionContext from the context map

    if (!ec) {
        throw new IllegalStateException("ExecutionContext (ec) is null. Ensure the service is called correctly.")
    }

    // Extract input parameters from the context map
    def trainingName = context.trainingName
    def trainingDate = context.trainingDate

    // Validate input parameters
    if (!trainingName) {
        ec.logger.error("trainingName is mandatory")
        throw new IllegalArgumentException("trainingName is mandatory")
    }

    def parsedDate = null
    try {
        def dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy")
        dateFormat.setLenient(false)
        parsedDate = dateFormat.parse(trainingDate)
    } catch (Exception e) {
        ec.logger.error("Invalid trainingDate format. Expected MM/dd/yyyy, but got: {}", trainingDate)
        throw new IllegalArgumentException("trainingDate must follow MM/dd/yyyy format")
    }

    ec.logger.info("Parsed trainingDate successfully: {}", parsedDate)

    try {
        // Create entity value
        EntityValue moquiTraining = ec.entity.makeValue("moqui.training.MoquiTraining")
        moquiTraining.set("trainingName", trainingName)
        moquiTraining.set("trainingDate", parsedDate)  // Ensure trainingDate is set correctly

        // Uncomment and add the necessary fields if needed
        // moquiTraining.set("trainingPrice", trainingPrice as BigDecimal)
        // moquiTraining.set("trainingDuration", trainingDuration as Integer)

        // Insert the entity into the database
        moquiTraining.create()

        ec.logger.info("Successfully inserted record with trainingId={}", moquiTraining.get("trainingId"))

        return [trainingId: moquiTraining.get("trainingId")]

    } catch (Exception e) {
        ec.logger.error("Error during entity creation: {}", e.getMessage())
        return [errorMessage: "Database error: ${e.getMessage()}"]
    }
}

// Example invocation of the service
return createMoquiTraining(context)
