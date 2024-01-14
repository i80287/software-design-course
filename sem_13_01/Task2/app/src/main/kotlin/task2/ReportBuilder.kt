package task2

class ReportBuilder(type: ReportType = ReportType.RESEARCH_REPORT) {
    var name: String = ""
        private set
    var type: ReportType = type
        private set
    var task: String = ""
        private set
    var annotation: String = ""
        private set
    var header: String = ""
        private set
    var body: String = ""
        private set
    var sources = ArrayList<String>()
        private set
    var appendix: String = ""
        private set

    fun withName(name: String): ReportBuilder {
        this.name = name
        return this
    }

    fun withType(type: ReportType): ReportBuilder {
        this.type = type
        return this
    }

    fun withTask(task: String): ReportBuilder {
        this.task = task
        return this
    }

    fun withAnnotation(annotation: String): ReportBuilder {
        this.annotation = annotation
        return this
    }

    fun withHeader(header: String): ReportBuilder {
        this.header = header
        return this
    }

    fun withBody(body: String): ReportBuilder {
        this.body = body
        return this
    }

    fun withAppendix(appendix: String): ReportBuilder {
        this.appendix = appendix
        return this
    }

    fun addSource(source: String): ReportBuilder {
        sources.add(source)
        return this
    }

    fun reserveSources(sourcesCapacity: Int) {
        sources.ensureCapacity(sourcesCapacity)
    }
    
    fun build(): Report = Report(name, type, task, annotation, header, body, sources, appendix)
}
