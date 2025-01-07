import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityCondition.ComparisonOperator

// Fetch MoquiTraining records
def fetchMoquiTraining(ExecutionContext ec) {
    // Get the input parameters from the execution context
    String trainingName = ec.context.trainingName
    String trainingId = ec.context.trainingId

    // Initialize the condition as null (it will be built based on input parameters)
    EntityCondition condition = null

    // Build condition for trainingName if provided
    if (trainingName) {
        condition = ec.entity.conditionFactory.makeCondition("trainingName", ComparisonOperator.EQUALS, trainingName)
    }

    // Build condition for trainingId if provided
    if (trainingId) {
        // If a condition already exists, combine it with the new condition
        if (condition) {
            condition = ec.entity.conditionFactory.makeCondition("trainingId", ComparisonOperator.EQUALS, trainingId)
                    .makeCondition(condition, EntityCondition.ComparisonOperator.AND) // Combine the conditions with AND
        } else {
            condition = ec.entity.conditionFactory.makeCondition("trainingId", ComparisonOperator.EQUALS, trainingId)
        }
    }

    // Fetch the training records based on the constructed condition
    def results = ec.entity.find("MoquiTraining")
            .condition(condition)
            .select("trainingId", "trainingName", "trainingDate")
            .list()

    // Return the results as a list of maps
    return results.collect { record ->
        [trainingId: record.trainingId,
         trainingName: record.trainingName,
         trainingDate: record.trainingDate]
    }

    ec.logger.info("Fetched MoquiTraining Data: ${results}");

}
