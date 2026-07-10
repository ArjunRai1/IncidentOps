export const getStatusVariant = (status) => {
    switch(status){
        case "OPEN":
            return "open";
        case "IN_PROGRESS":
            return "inProgress";
        case "RESOLVED":
            return "resolved";
        default:
            return "default";
    }
};

export const getPriorityVariant = (priority) => {
    switch(priority){
        case "HIGH":
            return "high";
        case "MEDIUM":
            return "medium";
        case "LOW":
            return "low";
        default:
            return "default";
    }
};

export const formatDate = (date) => {
    return new Date(date).toLocaleDateString();
};