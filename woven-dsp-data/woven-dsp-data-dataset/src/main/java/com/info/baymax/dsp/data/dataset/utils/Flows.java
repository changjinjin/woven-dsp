package com.info.baymax.dsp.data.dataset.utils;

import com.info.baymax.dsp.data.dataset.bean.InputOutputField;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowField;
import com.info.baymax.dsp.data.dataset.entity.core.LinkDesc;
import com.info.baymax.dsp.data.dataset.entity.core.ParameterDesc;
import com.info.baymax.dsp.data.dataset.entity.core.StepDesc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Flows {

    public static class StepBuilder {
        StepDesc step;

        public StepBuilder(String type, String id, String name) {
            step = new StepDesc(id, name, type, new ConfigObject(), new ArrayList<>(), new ArrayList<>(), 0, 0);
        }

        public StepBuilder input(List<FlowField> fields) {
            return inputWith("input", fields);
        }

        public StepBuilder inputWith(String name, List<FlowField> fields) {
            ConfigObject input = new ConfigObject();
            input.put("id", "input");
            input.put("name", name);
            input.put("fields", fields);
            step.getInputConfigurations().add(input);
            return this;
        }

        public StepBuilder inputWith(List<String> fields, List<String> alias) {
            ConfigObject input = new ConfigObject();
            input.put("id", "input");
            input.put("name", "input");
            ArrayList<InputOutputField> inputOutputFields = new ArrayList<>();
            for(int i=0 ; i<fields.size();i++){
                inputOutputFields.add(new InputOutputField(fields.get(i),alias.get(i)));
            }
            input.put("fields",inputOutputFields);
            //output.put("fields", Stream.of(fields).map(InputOutputField::new).collect(Collectors.toList()));
            step.getInputConfigurations().add(input);
            return this;
        }

        public StepBuilder output(List<FlowField> fields) {
            return outputWith("output", fields);
        }

        public StepBuilder outputWith(String name, List<FlowField> fields) {
            ConfigObject output = new ConfigObject();
            output.put("id", "output");
            output.put("name", name);
            output.put("fields", fields);
            step.getOutputConfigurations().add(output);
            return this;
        }

        public StepBuilder outputWith(List<String> fields, List<String> alias) {
            ConfigObject output = new ConfigObject();
            output.put("id", "output");
            output.put("name", "output");
            ArrayList<InputOutputField> inputOutputFields = new ArrayList<>();
            for(int i=0 ; i<fields.size();i++){
                inputOutputFields.add(new InputOutputField(fields.get(i),alias.get(i)));
            }
            output.put("fields",inputOutputFields);
            step.getOutputConfigurations().add(output);
            return this;
        }

        public StepBuilder config(String name, Object value) {
            step.getOtherConfigurations().put(name, value);
            return this;
        }

        public StepBuilder withPosition(int x, int y) {
            step.setX(x);
            step.setY(y);
            return this;
        }

        public StepDesc build() {
            StepDesc rt = new StepDesc(step.getId(), step.getName(), step.getType(), new ConfigObject(), new ArrayList<>(), new ArrayList<>(), step.getX(), step.getY());
            rt.getOtherConfigurations().putAll(step.getOtherConfigurations());
            rt.getInputConfigurations().addAll(step.getInputConfigurations());
            rt.getOutputConfigurations().addAll(step.getOutputConfigurations());
            return rt;
        }

    }

    public static class FlowBuilder {
        FlowDesc flow;

        public FlowBuilder(String name, String type) {
            flow = new FlowDesc(name, type, null, new ArrayList<>(), new ArrayList<>());
        }

        public FlowBuilder step(StepDesc step) {
            flow.getSteps().add(step);
            return this;
        }

        public FlowBuilder connect(String source, String output, String target, String input) {
            flow.getLinks().add(new LinkDesc(source+"-"+target, source, output, input, target));
            return this;
        }

        public FlowBuilder connect(String source, String target) {
            flow.getLinks().add(new LinkDesc(source+"-"+target, source, "output", "input", target));
            return this;
        }

        public FlowBuilder parameter(String name, String category, String defaultVal, String description, String... refs) {
            flow.getParameters().add(new ParameterDesc(name, category, Arrays.asList(refs), defaultVal, description));
            return this;
        }

        public FlowDesc build() {
            FlowDesc rt = new FlowDesc(flow.getName(), flow.getFlowType(), flow.getOid(), new ArrayList<>(), new ArrayList<>());
            rt.getLinks().addAll(flow.getLinks());
            rt.getSteps().addAll(flow.getSteps());
            rt.getParameters().addAll(flow.getParameters());
            return rt;
        }

    }

    public static FlowBuilder dataflow(String name) {
        return new FlowBuilder(name, "dataflow");
    }

    public static FlowBuilder streamflow(String name) {
        return new FlowBuilder(name, "streamflow");
    }

    public static StepBuilder step(String type, String id, String name) {
        return new StepBuilder(type, id, name);
    }

}
