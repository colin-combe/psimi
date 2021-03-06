package org.hupo.psi.calimocho.tab.model;

import org.hupo.psi.calimocho.model.DefinitionException;
import org.hupo.psi.calimocho.tab.io.FieldFormatter;
import org.hupo.psi.calimocho.tab.io.FieldParser;
import org.hupo.psi.calimocho.tab.io.formatter.LiteralFieldFormatter;
import org.hupo.psi.calimocho.tab.io.parser.LiteralFieldParser;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Training
 * Date: 23/03/11
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */
public class ColumnDefinitionBuilder {

    private AbstractColumnDefinition columnDefinition;

    public ColumnDefinitionBuilder() {
        this.columnDefinition = new AbstractColumnDefinition(){};
    }

    public ColumnDefinitionBuilder setName(String name){
        this.columnDefinition.setName(name);

        return this;
    }

    public ColumnDefinitionBuilder setDefinition(String definition){
        this.columnDefinition.setDefinition(definition);

        return this;
    }

    public ColumnDefinitionBuilder setPosition(Integer position){
        this.columnDefinition.setPosition(position);

        return this;
    }

    public ColumnDefinitionBuilder setKey(String key){
        this.columnDefinition.setKey( key );

        return this;
    }

    public ColumnDefinitionBuilder setFieldSeparator(String separator){
        this.columnDefinition.setFieldSeparator( separator );

        return this;
    }

    public ColumnDefinitionBuilder setFieldDelimiter(String delimiter){
        this.columnDefinition.setFieldDelimiter( delimiter );

        return this;
    }

    public ColumnDefinitionBuilder setEmptyValue(String value){
        this.columnDefinition.setEmptyValue( value );

        return this;
    }

    public ColumnDefinitionBuilder setIsAllowsEmpty(boolean isAllowsEmpty){
       this.columnDefinition.setAllowsEmpty( isAllowsEmpty );

        return this;
    }

    public ColumnDefinitionBuilder setFieldParser(FieldParser parser){
       this.columnDefinition.setFieldParser( parser );

        return this;
    }

    public ColumnDefinitionBuilder setFieldFormatter(FieldFormatter formatter){
       this.columnDefinition.setFieldFormatter( formatter );

        return this;
    }

    public ColumnDefinitionBuilder addDefaultValue(String key, String value){
       this.columnDefinition.getDefaultValues().put( key, value );

        return this;
    }

    public ColumnDefinitionBuilder addDefaultValues(Map<String,String> defaultsMap){
       this.columnDefinition.getDefaultValues().putAll( defaultsMap );

        return this;
    }

    public ColumnDefinitionBuilder extendColumnDefinition(ColumnDefinition colDef){

        if (colDef != null){
            columnDefinition.setEmptyValue(colDef.getEmptyValue());
            columnDefinition.setAllowsEmpty(colDef.isAllowsEmpty());
            columnDefinition.setDefinition(colDef.getDefinition());
            columnDefinition.setName(colDef.getName());
            columnDefinition.setFieldDelimiter(colDef.getFieldDelimiter());
            columnDefinition.setFieldFormatter(colDef.getFieldFormatter());
            columnDefinition.setFieldParser(colDef.getFieldParser());
            columnDefinition.setFieldSeparator(colDef.getFieldSeparator());
            columnDefinition.setKey(colDef.getKey());
            columnDefinition.setPosition(colDef.getPosition());
        }

        return this;
    }

    public void validate() throws DefinitionException {
        if ( columnDefinition.getKey() == null) {
            throw new DefinitionException( "No column key defined and it is mandatory" );
        }

        if ( columnDefinition.getPosition() == null) {
            throw new DefinitionException( "No column position defined and it is mandatory" );
        }

        if ( columnDefinition.getFieldFormatter() == null) {
            throw new DefinitionException( "No column field formatter defined and it is mandatory" );
        }

        if ( columnDefinition.getFieldParser() == null) {
            throw new DefinitionException( "No column field parser defined and it is mandatory" );
        }

        if( columnDefinition.getPosition() < 0 ) {
            throw new DefinitionException( "Column position must be 0 or higher: " + columnDefinition.getPosition() );
        }
    }

    public ColumnDefinition build() {
        if (columnDefinition.getFieldParser() == null) {
            columnDefinition.setFieldParser( new LiteralFieldParser() );
        }

        if (columnDefinition.getFieldFormatter() == null) {
            columnDefinition.setFieldFormatter( new LiteralFieldFormatter() );
        }

        validate();

        return columnDefinition;
    }
}
