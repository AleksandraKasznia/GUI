package dataFrame;

import myException.CustomException;
import value.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class DataFrame implements Cloneable{
    ArrayList<ArrayList> dataFrame;
    String[] names;
    ArrayList<Class<? extends Value>> types;
    public int numberOfColumns;


    public DataFrame(String[] names, ArrayList<Class<? extends Value>> types)throws CustomException {
        dataFrame = new ArrayList<>();
        this.names = names;
        this.types = types;
        this.numberOfColumns = names.length;
        initiate(types);
    }

    public DataFrame(DataFrame df){
        names = df.names;
        types = df.types;
        dataFrame = df.dataFrame;
        numberOfColumns = names.length;
    }

    public DataFrame(String fileName, ArrayList<Class<? extends Value>> types, boolean isHeader) throws IOException,
            NumberFormatException, CustomException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, InstantiationException{
        BufferedReader br = Files.newBufferedReader(Paths.get(fileName));
            dataFrame = new ArrayList<>();
            String line = br.readLine();
            if(line.split(",").length!=types.size()){
                throw new CustomException("Amount of given types can't differ from number of columns in file");
            }
            else {
                names = new String[types.size()];
                if (isHeader && line != null) {
                    names = line.split(",");
                    line = br.readLine();
                }
                initiate(types);
                this.types = types;

                while (line != null) {
                    String[] attributes = line.split(",");
                    add(attributes);
                    line = br.readLine();
                }
                numberOfColumns = names.length;
            }
    }

    public int size(){
        return dataFrame.get(0).size();
    }

    public ArrayList get(String colname) throws CustomException{
        int i;
        for (i=0; i<names.length; i++){
            if (names[i].equals(colname)){
                return dataFrame.get(i);
            }
        }
        throw new CustomException("There is no such column");
    }

    public Value getRecord(int column, int row){
        return (Value)dataFrame.get(column).get(row);
    }

    DataFrame get(String[] cols, boolean copy) throws CustomException{
        ArrayList list = new ArrayList();
        for (String col : cols) {
            for(int x =0; x<names.length; x++){
                if(col.equals(names[x])){
                    list.add(copy?dataFrame.get(x).clone():dataFrame.get(x));
                }
            }
        }
        if(list.size() == 0){
            throw new CustomException("No such column");
        }
        return new DataFrame(cols, list);
    }

    ArrayList getRow(int indexOfRow){
        ArrayList row = new ArrayList();
        for(int columnIterator=0; columnIterator<names.length; columnIterator++){
            row.add(dataFrame.get(columnIterator).get(indexOfRow));
        }
        return row;
    }

    public String getName(int index){
        return names[index];
    }

    DataFrame iloc(int i) throws CustomException{
        ArrayList list = new ArrayList();
        for (int k=0; k<dataFrame.size(); k++){
            if(dataFrame.get(k).size()>i){
                list.add(dataFrame.get(k).get(i));
            }
            else{
                return null;
            }
        }
        return new DataFrame(names, list);
    }

    DataFrame iloc(int from, int to) throws CustomException{
        ArrayList list = new ArrayList();
        ArrayList df = new ArrayList();
        for (int i=0; i<dataFrame.size(); i++){
            for (int k=from; k<to; k++){
                if(dataFrame.get(i).size()>k){
                    list.add(dataFrame.get(i).get(k));
                }
                else{
                    return null;
                }
            }
            df.add(list);
        }
        return new DataFrame(names, df);
    }

    void initiate(ArrayList<Class<? extends Value>> v) throws CustomException{
                for(int columnIterator=0; columnIterator<names.length; columnIterator++) {
                    if (Value.class.isAssignableFrom(v.get(columnIterator))) {
                        if (v.get(columnIterator) == IntHolder.class) {
                            dataFrame.add(new ArrayList<IntHolder>());
                        }
                        if (v.get(columnIterator) == DoubleHolder.class) {
                            dataFrame.add(new ArrayList<DoubleHolder>());
                        }
                        if (v.get(columnIterator) == FloatHolder.class) {
                            dataFrame.add(new ArrayList<FloatHolder>());
                        }
                        if (v.get(columnIterator) == StringHolder.class) {
                            dataFrame.add(new ArrayList<StringHolder>());
                        }
                        if (v.get(columnIterator) == DateTimeHolder.class) {
                            dataFrame.add(new ArrayList<DateTimeHolder>());
                        }

                    }
                    else{
                        throw new CustomException("Wrong type of input");
                    }
                }
    }

    void add(String [] content) throws NumberFormatException, CustomException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, InstantiationException{
        ArrayList <Value> values = new ArrayList<>();
        if(content.length > names.length){
            throw new CustomException("Too many arguments to add");
        }
        if(content.length < names.length){
            throw new CustomException("Too few arguments to add");
        }
            for (int columnIterator = 0; columnIterator < names.length; columnIterator++) {
                values.add(Value.builder(types.get(columnIterator)).build(content[columnIterator]));
            }

            addRow(values);
    }

    void addRow(ArrayList row){
        for(int columnIterator=0; columnIterator<names.length; columnIterator++){
            dataFrame.get(columnIterator).add(row.get(columnIterator));
        }
    }

    void addToColumn(String columnName, Value toAdd) throws CustomException{
            ArrayList column = get(columnName);
            if(!ifColumnIsNumeric(column)){
                throw new CustomException("Values are not numeric in this column");
            }
            int columnSize = column.size();
            for(int iterator=0; iterator<columnSize; iterator++){
                ((Value) column.get(iterator)).add(toAdd);
            }
    }

    void substractFromColumn(String columnName, Value toAdd) throws CustomException{
            ArrayList column = get(columnName);
            if(!ifColumnIsNumeric(column)){
                throw new CustomException("Values are not numeric in this column");
            }
            int columnSize = column.size();
            for(int iterator=0; iterator<columnSize; iterator++){
                ((Value) column.get(iterator)).sub(toAdd);
            }
    }

    void multiplyColumn(String columnName, Value toAdd){
        try{
            ArrayList column = get(columnName);
            if(!ifColumnIsNumeric(column)){
                throw new CustomException("Values are not numeric in this column");
            }
            int columnSize = column.size();
            for(int iterator=0; iterator<columnSize; iterator++){
                ((Value) column.get(iterator)).mul(toAdd);
            }
        }catch (CustomException e){
            e.printStackTrace();
        }

    }

    void divideColumn(String columnName, Value toAdd){
        try{
            ArrayList column = get(columnName);
            if(!ifColumnIsNumeric(column)){
                throw new CustomException("Values are not numeric in this column");
            }
            int columnSize = column.size();
            for(int iterator=0; iterator<columnSize; iterator++){
                ((Value) column.get(iterator)).div(toAdd);
            }
        }catch (CustomException e){
            e.printStackTrace();
        }

    }

    void addColumToColumn(String columnToAddTo ,String columnToAdd){
        try{
            ArrayList columnTAT = get(columnToAddTo);
            ArrayList columnTA = get(columnToAdd);
            if(!ifColumnIsNumeric(columnTA) || !ifColumnIsNumeric(columnTAT)){
                throw new CustomException("Those are not numeric columns");
            }
            int columnTATSize = columnTAT.size();
            int columnTASize = columnTA.size();
            if(columnTASize != columnTATSize){
                throw new CustomException("Columns are different sizes");
            }
            for(int iterator=0; iterator<columnTASize; iterator++){
                Value toAdd = (Value)((Value)columnTA.get(iterator)).clone();
                ((Value)columnTAT.get(iterator)).add(toAdd);
            }
        }catch (CustomException | CloneNotSupportedException e){
            e.printStackTrace();
        }

    }

    void substractColumnFromColumn(String columnToSubstractFrom ,String columnToSubstract){
        try{
            ArrayList columnTSF = get(columnToSubstractFrom);
            ArrayList columnTS = get(columnToSubstract);
            if(!ifColumnIsNumeric(columnTS) || !ifColumnIsNumeric(columnTSF)){
                throw new CustomException("Those are not numeric columns");
            }
            int columnTAFSize = columnTSF.size();
            int columnTSSize = columnTS.size();
            if(columnTSSize != columnTAFSize){
                throw new CustomException("Columns are different sizes");
            }
            for(int iterator=0; iterator<columnTSSize; iterator++){
                Value toAdd = (Value)((Value)columnTS.get(iterator)).clone();
                ((Value)columnTSF.get(iterator)).sub(toAdd);
            }
        }catch (CustomException | CloneNotSupportedException e){
            e.printStackTrace();
        }

    }

    void multiplyColumByColumn(String columnToBeMultipied ,String columnToMultiplyBy){
        try{
            ArrayList columnTBM = get(columnToBeMultipied);
            ArrayList columnTMB = get(columnToMultiplyBy);
            if(!ifColumnIsNumeric(columnTMB) || !ifColumnIsNumeric(columnTBM)){
                throw new CustomException("Those are not numeric columns");
            }
            int columnTBMSize = columnTBM.size();
            int columnTMBSize = columnTMB.size();
            if(columnTMBSize != columnTBMSize){
                throw new CustomException("Columns are different sizes");
            }
            for(int iterator=0; iterator<columnTMBSize; iterator++){
                Value toAdd = (Value)((Value)columnTMB.get(iterator)).clone();
                ((Value)columnTBM.get(iterator)).mul(toAdd);
            }
        }catch (CustomException | CloneNotSupportedException e){
            e.printStackTrace();
        }

    }

    void divideColumnByColumn(String columnToBeDivided ,String columnToDivideBy){
        try{
            ArrayList columnTBD = get(columnToBeDivided);
            ArrayList columnTDB = get(columnToDivideBy);
            if(!ifColumnIsNumeric(columnTDB) || !ifColumnIsNumeric(columnTBD)){
                throw new CustomException("Those are not numeric columns");
            }
            int columnTBMDSize = columnTBD.size();
            int columnTDBSize = columnTDB.size();
            if(columnTDBSize != columnTBMDSize){
                throw new CustomException("Columns are different sizes");
            }
            for(int iterator=0; iterator<columnTDBSize; iterator++){
                Value toAdd = (Value)((Value)columnTDB.get(iterator)).clone();
                ((Value)columnTBD.get(iterator)).div(toAdd);
            }
        }catch (CustomException | CloneNotSupportedException e){
            e.printStackTrace();
        }

    }

    LinkedList<DataFrame> groupbyOne(String colname) throws CustomException{
        LinkedList<DataFrame> splitDataFrame = new LinkedList<DataFrame>();
            Set<Value> uniqueValues = new HashSet(get(colname));
            Iterator<Value> it = uniqueValues.iterator();
            while(it.hasNext()){
                splitDataFrame.add(createDataFrameForGivenValue(it.next(),colname));
            }
        return splitDataFrame;
    }

    public SplitData groupby(String[] colnames)throws CustomException{
        LinkedList<LinkedList<DataFrame>> tmp = new LinkedList <>();
        LinkedList<DataFrame> splitDataFrame = new LinkedList<DataFrame>(groupbyOne(colnames[0]));

            for (int colnamesIterator = 1; colnamesIterator < colnames.length; colnamesIterator++) {
                for (int holderIterator = 0; holderIterator < splitDataFrame.size(); holderIterator++) {
                    tmp.add(splitDataFrame.get(holderIterator).groupbyOne(colnames[colnamesIterator]));
                }
                splitDataFrame = flattenLinkedList(tmp);
                tmp.clear();
            }
            return new SplitData(colnames,splitDataFrame);
    }

    public SplitData groupby() throws CustomException{
        LinkedList<DataFrame> list = new LinkedList<>();
        list.add(this);
        String[] names = new String[]{};
        return new SplitData(names,list);
    }

    DataFrame createDataFrameForGivenValue(Value value, String colname) throws CustomException{
        DataFrame dataFrameOfValue = new DataFrame(names,types);
        int indexOfColumn = 0;
        while(!names[indexOfColumn].equals(colname)){
            indexOfColumn++;
        }

        for(int indexOfElement=dataFrame.get(indexOfColumn).indexOf(value); indexOfElement<dataFrame.get(indexOfColumn).size(); indexOfElement++){
            if(dataFrame.get(indexOfColumn).get(indexOfElement).equals(value)){
                dataFrameOfValue.addRow(getRow(indexOfElement));
            }
        }

        return dataFrameOfValue;
    }

    LinkedList<DataFrame> flattenLinkedList(LinkedList<LinkedList<DataFrame>> linkedListOfLinkedLists){
        LinkedList<DataFrame> flattenList = new LinkedList<>();
        for(int listIterator=0; listIterator<linkedListOfLinkedLists.size(); listIterator++){
            flattenList.addAll(linkedListOfLinkedLists.get(listIterator));
        }
        return flattenList;
    }

    public boolean ifColumnIsNumeric(ArrayList column){
        return (column.get(0) instanceof IntHolder | column.get(0) instanceof DoubleHolder | column.get(0)
                instanceof FloatHolder | column.get(0) instanceof DateTimeHolder);
    }

    public class SplitData implements GroupBy {
        LinkedList<DataFrame> listOfSplitDataFrames;
        DataFrame output;
        ArrayList<Value> row;
        ArrayList<Class> typesOfOutput;
        String[] namesToGroupBy;
        ArrayList<String> namesOfOutput;

        SplitData(String[] colnames, LinkedList<DataFrame> listOfSplitDataFrames) throws CustomException{
            namesToGroupBy = colnames;
            this.listOfSplitDataFrames=listOfSplitDataFrames;
            row = new ArrayList<>();
            namesOfOutput = new ArrayList<>();
            typesOfOutput = new ArrayList<>();
            output = new DataFrame(names, types);
        }

        @Override
        public DataFrame max() {
            for (DataFrame data: listOfSplitDataFrames){
                row.clear();
                for(ArrayList<? extends Value> column: data.dataFrame){
                    row.add(Collections.max(column));
                }
                output.addRow(row);
            }

            return output;
        }

        @Override
        public DataFrame min() {
            for (DataFrame data: listOfSplitDataFrames){
                row.clear();
                for(ArrayList<? extends Value> column: data.dataFrame){
                    row.add(Collections.min(column));
                }
                output.addRow(row);
            }

            return output;
        }

        public ArrayList<Value> meanOfColumn(DataFrame data) throws CloneNotSupportedException, CustomException{
            Value sum;
            int rowIterator;
                row.clear();
                for(ArrayList<? extends Value> column: data.dataFrame){
                    if(ifColumnIsNumeric(column)){
                        sum = (Value) column.get(0).clone();

                        for(rowIterator=0; rowIterator<column.size(); rowIterator++){
                            if(rowIterator!=0){
                                sum.add((Value)column.get(rowIterator).clone());
                            }
                        }
                            row.add(sum.div(new IntHolder(rowIterator)));
                    }
                    else{
                        row.add(null);
                    }
                }
                return row;
        }

        @Override
        public DataFrame mean() throws CustomException, CloneNotSupportedException{
            ArrayList<Value> rowOfMeans;
            int index;

                for (DataFrame data : listOfSplitDataFrames) {
                    rowOfMeans = new ArrayList<>(meanOfColumn(data));
                    row.clear();
                    index = 0;
                    for (ArrayList<? extends Value> column : data.dataFrame) {
                        if (Arrays.stream(namesToGroupBy).anyMatch(names[index]::equals)) {
                            row.add(column.get(0));
                        } else {
                            row.add(rowOfMeans.get(index));

                        }
                        index++;
                    }

                    output.addRow(row);
                }
            return output;
        }

        @Override
        public DataFrame median(){
            for (DataFrame data: listOfSplitDataFrames){
                row.clear();
                for(ArrayList<? extends Value> column: data.dataFrame){
                    Collections.sort(column);
                    if(column.size()%2 == 1){
                        row.add(column.get(column.size()/2).add(column.get(size()/2+1)));
                    }
                    else{
                        row.add(column.get(column.size()/2));
                    }
                }
                output.addRow(row);
            }

            return output;
        }

        @Override
        public DataFrame std() throws CustomException, CloneNotSupportedException{
            ArrayList<Value> rowOfMeans;
            Value sum;
            Value distance;
            Value mean;
            Value val;
            int index;
            int rowIterator;

                for (DataFrame data : listOfSplitDataFrames) {
                    rowOfMeans = new ArrayList<>(meanOfColumn(data));
                    row.clear();
                    index = 0;
                    for (ArrayList<? extends Value> column : data.dataFrame) {
                        if (Arrays.stream(namesToGroupBy).anyMatch(names[index]::equals)) {
                            row.add((Value) column.get(0).clone());
                        } else {
                            if (rowOfMeans.get(index) != null) {
                                mean = (Value) rowOfMeans.get(index).clone();
                                val = (Value) column.get(0).clone();
                                distance = val.sub(mean);
                                sum = (Value) distance.clone();
                                sum = sum.mul(sum);
                                for (rowIterator = 0; rowIterator < column.size(); rowIterator++) {
                                    if (rowIterator != 0) {
                                        val = (Value) column.get(rowIterator).clone();
                                        distance = val.sub(mean);
                                        sum.add(distance.mul(distance));
                                    }
                                }
                                if (rowIterator <= 1) {
                                    rowIterator = 2;
                                }
                                    row.add(sum.div(new IntHolder(rowIterator - 1)).pow(new DoubleHolder(0.5)));
                            } else {
                                row.add(null);
                            }
                        }
                        index++;
                    }

                    output.addRow(row);
                }
            return output;
        }

        @Override
        public DataFrame sum() {
            Value sum;
            int rowIterator;
            for (DataFrame data: listOfSplitDataFrames){
                row.clear();
                for(ArrayList<? extends Value> column: data.dataFrame){
                    if(ifColumnIsNumeric(column)){
                        sum = column.get(0);

                        for(rowIterator=0; rowIterator<column.size(); rowIterator++){
                            if(rowIterator!=0){
                                sum.add(column.get(rowIterator));
                            }
                        }
                        row.add(sum);
                    }
                    else{
                        row.add(null);
                    }
                }
                output.addRow(row);
            }

            return output;
        }

        @Override
        public DataFrame var() throws CustomException, CloneNotSupportedException{
            ArrayList<Value> rowOfMeans;
            Value sum;
            Value distance;
            Value mean;
            Value val;
            int index;
            int rowIterator;

                for (DataFrame data : listOfSplitDataFrames) {
                    rowOfMeans = new ArrayList<>(meanOfColumn(data));
                    row.clear();
                    index = 0;
                    for (ArrayList<? extends Value> column : data.dataFrame) {
                        if (Arrays.stream(namesToGroupBy).anyMatch(names[index]::equals)) {
                            row.add((Value) column.get(0).clone());
                        } else {
                            if (rowOfMeans.get(index) != null) {
                                mean = (Value) rowOfMeans.get(index).clone();
                                val = (Value) column.get(0).clone();
                                distance = val.sub(mean);
                                sum = (Value) distance.clone();
                                sum = sum.mul(sum);
                                for (rowIterator = 0; rowIterator < column.size(); rowIterator++) {
                                    if (rowIterator != 0) {
                                        val = (Value) column.get(rowIterator).clone();
                                        distance = val.sub(mean);
                                        sum.add(distance.mul(distance));
                                    }
                                }
                                if (rowIterator <= 1) {
                                    rowIterator = 2;
                                }
                                    row.add(sum.div(new IntHolder(rowIterator - 1)));
                            } else {
                                row.add(null);
                            }
                        }
                        index++;
                    }

                    output.addRow(row);
                }
            return output;
        }

        @Override
        public DataFrame apply(Applyable a) {
            try {
                DataFrame output = new DataFrame(names, types);

                for (DataFrame df : listOfSplitDataFrames) {
                    DataFrame oneOfSplitData = a.apply((DataFrame) df.clone());
                    for (int rowIterator = 0; rowIterator < df.names.length; rowIterator++) {
                        output.addRow(oneOfSplitData.getRow(rowIterator));
                    }
                }
            }catch(CloneNotSupportedException|CustomException e){
                    e.printStackTrace();
                }
            return output;
        }
    }

}
