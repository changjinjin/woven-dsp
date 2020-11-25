package com.info.baymax.dsp.data.dataset.utils;

import com.info.baymax.dsp.data.dataset.bean.InputOutputField;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowField;
import com.info.baymax.dsp.data.dataset.entity.core.StepDesc;
import com.merce.woven.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Flows {

    public static class StepBuilder {
        StepDesc step;

        public StepBuilder(String type, String id, String name) {
            step = new StepDesc(id, name, type, new ConfigObject(), new StepFieldGroup(), new StepFieldGroup(), 0, 0);
        }

        public StepBuilder input(List<FlowField> fields) {
            return inputWith("input", fields);
        }

        public StepBuilder inputWith(String name, List<FlowField> fields) {
            step.getInputConfigurations().put("input",
                fields.stream()
                    .map(t -> new FieldDesc(t.getColumn(), t.getType(), t.getAlias(), t.getDescription()))
                    .collect(Collectors.toList()));
            return this;
        }

        public StepBuilder inputWith(List<String> fields, List<String> alias) {
            ArrayList<InputOutputField> inputOutputFields = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                inputOutputFields.add(new InputOutputField(fields.get(i), alias.get(i)));
            }
            step.getInputConfigurations().put("input", inputOutputFields.stream()
                .map(t -> new FieldDesc(t.getColumn(), t.getAlias())).collect(Collectors.toList()));
            return this;
        }

        public StepBuilder output(List<FlowField> fields) {
            return outputWith("output", fields);
        }

        public StepBuilder outputWith(String name, List<FlowField> fields) {
            step.getOutputConfigurations().put("output",
                fields.stream()
                    .map(t -> new FieldDesc(t.getColumn(), t.getType(), t.getAlias(), t.getDescription()))
                    .collect(Collectors.toList()));
            return this;
        }

        public StepBuilder outputWith(List<String> fields, List<String> alias) {
            ArrayList<InputOutputField> inputOutputFields = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                inputOutputFields.add(new InputOutputField(fields.get(i), alias.get(i)));
            }
            step.getOutputConfigurations().put("output", inputOutputFields.stream()
                .map(t -> new FieldDesc(t.getColumn(), t.getAlias())).collect(Collectors.toList()));
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

        public StepBuilder inputConfigurations(StepFieldGroup inputConfigurations) {
            step.setInputConfigurations(inputConfigurations);
            return this;
        }

        public StepBuilder outputConfigurations(StepFieldGroup outputConfigurations) {
            step.setOutputConfigurations(outputConfigurations);
            return this;
        }

        public StepBuilder otherConfigurations(ConfigObject otherConfigurations) {
            step.setOtherConfigurations(otherConfigurations);
            return this;
        }

        public StepBuilder uiConfigurations(ConfigObject uiConfigurations) {
            step.setUiConfigurations(uiConfigurations);
            return this;
        }

        public StepDesc build() {
            StepDesc rt = new StepDesc(step.getId(), step.getName(), step.getType(), new ConfigObject(),
                new StepFieldGroup(), new StepFieldGroup(), step.getX(), step.getY());
            rt.getOtherConfigurations().putAll(step.getOtherConfigurations());
            rt.getInputConfigurations().putAll(step.getInputConfigurations());
            rt.getOutputConfigurations().putAll(step.getOutputConfigurations());
            rt.setUiConfigurations(step.getUiConfigurations());
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
            flow.getLinks().add(new LinkDesc(source + "-" + target, source, output, input, target));
            return this;
        }

        public FlowBuilder connect(String source, String target) {
            flow.getLinks().add(new LinkDesc(source + "-" + target, source, "output", "input", target));
            return this;
        }

        public FlowBuilder parameter(String name, String category, String defaultVal, String description,
                                     String... refs) {
            flow.getParameters().add(new ParameterDesc(name, category, Arrays.asList(refs), defaultVal, description));
            return this;
        }

        public FlowDesc build() {
            FlowDesc rt = new FlowDesc(flow.getName(), flow.getFlowType(), flow.getOid(), new ArrayList<>(),
                new ArrayList<>());
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
