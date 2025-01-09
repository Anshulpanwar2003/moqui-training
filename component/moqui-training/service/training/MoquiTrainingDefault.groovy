import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityValue

ExecutionContext ec = context.ec


def trainingName = context.trainingName
def trainingDate = context.trainingDate
def trainingPrice = context.trainingPrice
def trainingDuration = context.trainingDuration


if (!trainingName) {
    throw new Exception("Training name is required.")
    return;
}
if (!trainingDate) {
    throw new Exception("Training date is required.")
    return;
}

def trainingId = ec.entity.sequencedIdPrimary("MoquiTraining")


EntityValue trainingRecord = ec.entity.makeValue("moqui.training.MoquiTraining")
trainingRecord.setAll([
        trainingId     : trainingId,
        trainingName   : trainingName,
        trainingDate   : trainingDate,
        trainingPrice  : trainingPrice ?: null,
        trainingDuration: trainingDuration ?: null
])


trainingRecord = trainingRecord.create()

context.trainingId = trainingRecord.trainingId
