module shift.common {
    exports ru.shift;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires static lombok;
    requires org.slf4j;

    opens ru.shift to com.fasterxml.jackson.databind;
}