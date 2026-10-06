/*
Created by James Casongsong(JC)

Guide for NOTES
    CAPS = known limitations
    lowercase = future plans(?)

 NOTES:
 1. NEGATIVE NUMBERS IN EITHER SQUAREROOT & LOG ISN'T SUPPORTED(imaginary numbers aren't implemented)
 2. MOST OF THESE RELIES EACH OTHER(just a quick heads up if changing something in the future)
 3. NESTED SQUAREROOTS OR LOGS ISN'T SUPPORTED
 4. Maybe try to refactor some parts of the code?(mostly on Restructure_input and Maththis)
 5. Understand some of java's syntaxes?(like java's abstraction, considering Java awt and swing have methods that primarily use java's abstraction)
 */



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Insets;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;

class Draw extends JComponent{
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,300,100);
    }
}
public class Java_swing_example
{
    static ArrayList<String> inputs = new ArrayList<String>();
    static JLabel text;
    static ArrayList<String> special_arithmetic_operations = new ArrayList<>();
    static {Collections.addAll(special_arithmetic_operations, "√", "log");}
    public static void main(String[] args){
        int height = 400;
        int width = 300;
        JFrame frame = GUI(width,height);
        GridBagConstraints pos = new GridBagConstraints();
        
        
        GridBagConstraints pos_panel = new GridBagConstraints();
        JPanel black_screen = DisplayArea(new JPanel());
        pos_panel.ipady = 100;
        pos_panel.ipadx = 300;
        pos_panel.anchor = GridBagConstraints.CENTER;
        pos_panel.gridy = 0;
        pos_panel.gridx = 0;
        black_screen.add(new Draw(),pos_panel);
        pos_panel.ipady = 0;
        pos_panel.ipadx = 0;
        text = new JLabel("");
        text.setForeground(Color.GREEN);
        text.setFont(new Font("Monospaced", Font.BOLD, 50));
        pos.gridx = 0;
        pos.gridy = 0;
        
        pos.insets = new Insets(30,0,0,10);
        pos.anchor = GridBagConstraints.LAST_LINE_END;
        frame.add(text, pos);
        
        
        pos.insets = new Insets(0,0,0,0);
        pos.fill = GridBagConstraints.BOTH;
        frame.add(black_screen,pos);
        


        JPanel buttons_panel = ButtonArea();
        String[][] buttons_text = {
            {"1","2","3","/","AC"},
            {"4","5","6","*","="},
            {"7","8","9","-","√"},
            {"<html>+<br>-</html>","0",".","+","log"}
        };
        Map<String, JButton> buttons = Button_initializer(buttons_text, buttons_panel); //Another dictionary thing that uses string variable to name stuff!
        
        pos.gridx = 0;
        pos.gridy = 1;
        pos.weightx = 1;
        pos.ipady = 125;
        pos.ipadx = 0;
        pos.anchor = GridBagConstraints.PAGE_END;
        pos.fill = GridBagConstraints.HORIZONTAL;
        frame.add(buttons_panel,pos);
    }
    
    /**
     * A simple Button initializer,
     * to keep the code small for main
     */
    private static Map<String, JButton> Button_initializer(String[][]buttons_list, JPanel btn_panel){
        GridBagConstraints button_pos = new GridBagConstraints();
        button_pos.weightx = 1;
        button_pos.weighty = 1;
        button_pos.fill = GridBagConstraints.BOTH;
        Map<String, JButton> x = new HashMap<>();
        for (int i = 0, button_index=1; i<buttons_list.length; i++){
            for (int j = 0; j<buttons_list[i].length; j++,button_index++){
                button_pos.gridy = i;
                button_pos.gridx = j;
                x.put(String.format("Button%s", button_index), Button(buttons_list[i][j], btn_panel, button_pos));
            }
        }

        //Customization section for buttons(for aesthetic purposes)
        x.get("Button15").setFont(new Font("Monospaced", Font.BOLD, 15));

        return x;
    }
    

    /**
     * Self explanatory method,
     * checks if the list have something,
     * if b has atleast any of a's elements
     */
    private static Boolean itContains(ArrayList<String> a, String b){
        for (String x : a){
            if (b.contains(x)){
                return true;
            }
        }
        return false;
    }

    /**
     * Self explanatory method,
     * to check if it can be a number
     * (either an integer or a double, thanks to BigDecimal)
     * **numbers(which each are length>1) with special mathematical operations are considered
     *   numbers
     */
    private static Boolean isNumber(String x){
        try {
            if (x.length()>1){
                for (String spcop : special_arithmetic_operations){
                    if (x.contains(spcop)){
                        x = x.replace(spcop,"");
                    }
                }
            }
            new BigDecimal(x);
            return true;
        } 
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Self explanatory method,
     * it also checks if it is a number(first)
     */
    private static Boolean isDecimal(String x){
        if (!isNumber(x)){
            return false;
        }
        return x.contains(".");
    }
    
    /**
     * An important method for Maththis,
     * a method for determing a decimal number in a list,
     * used to determine if Maththis's return is a decimal or not
     */
    private static Boolean Has_a_decimal_number(ArrayList<String> L){
        for (String x : L){
            if (isDecimal(x)){
                return true;
            }
        }
        return false;
    }

    /**
     * The backbone of Restructure_input,
     * Does the arithmetic operations,
     * Only accomodates Squareroot--squarerooting is automatically becomes a decimal!
     * returns String
     */
    private static String Maththis(ArrayList<String> List){
        boolean has_sqrt = String.join("", List).contains("√");
        boolean has_log = String.join("", List).contains("log");
        if (Has_a_decimal_number(List) || has_sqrt||has_log){
            if (List.get(0).contains("√")){
                List.set(0, String.format("%s",Math.sqrt(Double.parseDouble(List.get(0).replace("√","")))));
            }
            else if (List.get(0).contains("log")){
                List.set(0, String.format("%s", Math.log10(Double.parseDouble(List.get(0).replace("log","")))));
            }
            Double total = Double.parseDouble(List.get(0));
            for (int i=1;i<List.size();i+=2){
                if (List.get(i+1).contains("√")){
                    List.set(i+1, String.format("%s",Math.sqrt(Double.parseDouble(List.get(i+1).replace("√","")))));
                }
                else if (List.get(i+1).contains("log")){
                    List.set(i+1, String.format("%s", Math.log10(Double.parseDouble(List.get(i+1).replace("log","")))));
                }
                String operator = List.get(i);
                Double second = Double.parseDouble(List.get(i+1));
                switch(operator){
                    case "+" -> total +=second;
                    case "-" -> total -=second;
                    case "*" -> total *=second;
                    case "/" -> {
                        if (second==0) {
                            return "UNDEFINED";
                        } 
                        else {
                            total /=second;
                        }
                    }
                }
            }
            DecimalFormat round = new DecimalFormat("0.0##");
            return String.format("%s",round.format(total));
        }
        else{
            Integer total = Integer.parseInt(List.get(0));
            for (int i=1;i<List.size();i+=2){
                String operator = List.get(i);
                Integer second = Integer.parseInt(List.get(i+1));
                switch(operator){
                    case "+" -> total +=second;
                    case "-" -> total -=second;
                    case "*" -> total *=second;
                    case "/" -> {
                        if (second==0) {
                            return "UNDEFINED";
                        } 
                        else {
                            total /=second;
                        }
                    }
                }
            }
            return String.format("%s",total);
        }
    }
    
    /**
     * The headmaster of calculating the user's inputs,
     * and ensures a proper text output(arranges the array named inputs),
     * Adjusts mainly an array called inputs(defined in this program),
     * Uses Maththis for the arithmetic operations
     */
    private static void Restructure_input(String x){

        //ALL CLEAR functionality
        if (x.equals("AC")){
            inputs.clear();
        }
        //Enter functionality
        else if (x.equals("=") && !inputs.isEmpty()){
            String result = Maththis(inputs);
            inputs.clear();
            inputs.add(result);
        }
        
        //To prevent non numbers, except things like √
        else if (inputs.isEmpty()){
            if (isNumber(x)){
                inputs.add(x);
            }
            else if (x.equals("√")){
                inputs.add(x);
            }
            else if (x.equals("log")){
                inputs.add(x);
            }
        }
        //The calculation of this method
        else{
            boolean is_previous_a_basic_operator = !isNumber(inputs.get(inputs.size()-1)) && !itContains(special_arithmetic_operations, inputs.get(inputs.size()-1));
            boolean is_prev_a_num = isNumber(inputs.get(inputs.size()-1));
            boolean does_prev_have_a_decimal = inputs.get(inputs.size()-1).contains(".");
            boolean is_now_a_num = isNumber(x);
            boolean can_merge_numbers = is_prev_a_num && is_now_a_num;
            boolean is_prev_a_zero = is_prev_a_num && inputs.get(inputs.size()-1).equals("0");
            boolean does_prev_have_special_op = itContains(special_arithmetic_operations, inputs.get(inputs.size()-1));

            //Number calculations
            if (isNumber(x)){
                if (is_prev_a_zero){
                    inputs.set(inputs.size()-1, x);
                }
                else if (can_merge_numbers || does_prev_have_special_op){
                    inputs.set(inputs.size()-1, inputs.get(inputs.size()-1)+x);
                }
                else{
                    inputs.add(x);
                }
            }

            //Change number to either negative or positive
            else if(x.equals("<html>+<br>-</html>")){
                String trimmed_number = inputs.get(inputs.size()-1);

                if (does_prev_have_special_op){
                    for (String spcop : special_arithmetic_operations){
                        if (trimmed_number.contains(spcop)){
                            trimmed_number = trimmed_number.replace(spcop, "");
                        }
                    }
                }

                if (is_prev_a_num && !trimmed_number.contains("-")){
                    String applyit = inputs.get(inputs.size()-1).replace(trimmed_number, "-" + trimmed_number);
                    inputs.set(inputs.size()-1, applyit);
                }
                else if (is_prev_a_num && trimmed_number.contains("-")){
                    String applyit = trimmed_number.replace("-","");
                    inputs.set(inputs.size()-1, inputs.get(inputs.size()-1).replace(trimmed_number, applyit));
                }
            }
            //Change function upon arithmetic symbols or special arithmetic symbols
            else{
                //To add special_arithmetic_operations if the previous is a BASIC operator
                if (is_previous_a_basic_operator && itContains(special_arithmetic_operations,x)){
                    inputs.add(x);
                }
                //To add ONLY IF X IS A BASIC operator and the prev. is a number
                else if (is_prev_a_num && !x.equals(".") && !itContains(special_arithmetic_operations,x)){
                    inputs.add(x);
                }
                //To add the decimal point
                else if (is_prev_a_num && x.equals(".") && !does_prev_have_a_decimal){
                    inputs.set(inputs.size()-1, inputs.get(inputs.size()-1)+x); 
                }
                //Replaces the previous basic operator if x is a BASIC operator
                else if (is_previous_a_basic_operator && !x.equals(".") && !itContains(special_arithmetic_operations,x)){
                    inputs.set(inputs.size()-1, x);
                }
            }
        }
        /* Complex and condensed, but does the merging between two elements if both are integers
        try{
            if (inputs.size()>0){
                Integer.parseInt(inputs.get(inputs.size()-1));
                inputs.set(inputs.size()-1, inputs.get(inputs.size()-1) + Integer.toString(Integer.parseInt(x)));
            }
            else{
                inputs.add(Integer.toString(Integer.parseInt(x)));
            }
        }
        catch(NumberFormatException e){
            inputs.add(x);
        } 
        */
    }
    
    /**
     * MUST BE CALLED AFTER Restructure_input(it depends on it)
     * - Checks if there is a literal string in the first
     * -^ element of the inputs AND doesnt have elements from exlude
     * -^ if so then an error had occured
     * -^ it's how to determine if an error has occurred
     */
    private static boolean ErrorHandling(){
        if (!inputs.isEmpty() && (!isNumber(inputs.get(0)) && !itContains(special_arithmetic_operations, inputs.get(0)))){
            return true;
        }
        return false;
    }
    
    /**
     * Processes inputs,
     * Uses Restructure_input as the main component for the calculation
     * Uses ErrorHandling for properly addressing errors during the calculation
     * - for clearing the inputs, as we only want it to display the error
     * -^ while not affecting the inputs(removing the error in the inputs's array)
     */
    private static void Process_input(JButton x){
        Restructure_input(x.getText());
        text.setText(String.join("",inputs));
        if (ErrorHandling()){
            text.setText("ERROR!");
            inputs.clear();
        }

        //DEBUG SECTION===================================================================================================================================================================
        //System.out.println(inputs);
    }
    
    /**
     * An initialization for Display area,
     * the display component of the calculator
     * returns a JPanel variable to access it outside
     */
    private static JPanel DisplayArea(JPanel x){
        x.setVisible(true);
        x.setLayout(new GridBagLayout());
        return x;
    }
    
    /**
     * An initialization for Buttons
     * the buttons of the calculator
     * returns a JPanel variable to access it outside
     */
    private static JPanel ButtonArea(){
        JPanel p = new JPanel();
        p.setVisible(true);
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.GRAY);
        return p;
    }

    /**
     * An initialization for the whole frame
     * the main frame component of the calculator
     * returns a JFrame variable to access it outside
     */
    private static JFrame GUI(int h, int w){
        JFrame mainframe = new JFrame("Calc");
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setResizable(false);
        mainframe.setVisible(true);
        mainframe.setSize(h,w);
        mainframe.setLayout(new GridBagLayout());
        return mainframe;
    }

    /**
     * A button initializator for the calculator
     * returns a JButton variable to access it outside
     */
    private static JButton Button(String text, JPanel p, GridBagConstraints pos){
        JButton btn = new JButton(text);
        btn.setBackground(Color.GRAY);
        btn.setForeground(new Color(0,71,186));
        btn.setFocusPainted(false);
        //btn.setPreferredSize(new Dimension(height,width));
        btn.setFont(new Font("Monospaced", Font.BOLD, 20));
        //btn.setPreferredSize(new Dimension(height,width));
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Process_input(btn);
            }
        });
        p.add(btn, pos);
        return btn;
    }

}