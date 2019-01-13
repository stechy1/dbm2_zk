package cz.pstechmu.dbm2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

    // region Constants

    private static final String FILE_PATH = "/DBM2_zkouska_data.csv";

    // endregion

    // region Variables

    // region FXML

    @FXML
    private VBox vbActionCharacter;
    @FXML
    private VBox vbBase;

    @FXML
    ComboBox<EventType> cmbTime1;
    @FXML
    ComboBox<EventType> cmbTime2;
    @FXML
    LineChart<String, Long> lineChart;

    // endregion

    private final List<DataSource> data = new ArrayList<>();
    private final XYChart.Series<String, Long> averageSeries = new Series<>();
    private final XYChart.Series<String, Long> minSeries = new Series<>();
    private final XYChart.Series<String, Long> maxSeries = new Series<>();

    private final boolean[] usedActionCharacters = new boolean[ActionCharacter.values().length];
    private final boolean[] usedEmergencyBases = new boolean[EmergencyBase.values().length];

    // endregion

    // region Private methods

    private void loadData() {
        List<String> lines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(FILE_PATH)))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            data.addAll(lines.stream()
                // Musí obsahovat všechny záznamy
                .filter(s -> !s.contains("BLANK"))
                .map(DataSource::new)
                // Všechny údaje musí být správně naparsovány
                .filter(DataSource::isValid)
                .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initActionCharacters() {
        Arrays.stream(ActionCharacter.values())
            .forEach(actionCharacter -> {
                final CheckBox checkBox = new CheckBox(actionCharacter.description);
                checkBox.setSelected(true);
                checkBox.setUserData(actionCharacter);
                checkBox.setOnAction(this::actionCharacterChange);
                vbActionCharacter.getChildren().add(checkBox);
            });
    }

    private void actionCharacterChange(ActionEvent actionEvent) {
        CheckBox sourceCheckBox = (CheckBox) actionEvent.getSource();
        ActionCharacter actionCharacter = (ActionCharacter) sourceCheckBox.getUserData();
        usedActionCharacters[actionCharacter.ordinal()] = sourceCheckBox.isSelected();
        updateChart();
    }

    private void initBases() {
        Arrays.stream(EmergencyBase.values())
            .forEach(emergencyBase -> {
                final CheckBox checkBox = new CheckBox(emergencyBase.cityName);
                checkBox.setSelected(true);
                checkBox.setUserData(emergencyBase);
                checkBox.setOnAction(this::actionBaseChange);
                vbBase.getChildren().add(checkBox);
            });
    }

    private void actionBaseChange(ActionEvent actionEvent) {
        CheckBox sourceCheckBox = (CheckBox) actionEvent.getSource();
        EmergencyBase emergencyBase = (EmergencyBase) sourceCheckBox.getUserData();
        usedEmergencyBases[emergencyBase.ordinal()] = sourceCheckBox.isSelected();
        updateChart();
    }

    private void updateChart() {
        EventType eventType1 = cmbTime1.getValue();
        EventType eventType2 = cmbTime2.getValue();
        Map<EmergencyBase, List<Duration>> map = new HashMap<>(EmergencyBase.values().length);

        for (DataSource source : data) {
            // Filter charakteru akcí
            if (!usedActionCharacters[source.getActionCharacter().ordinal()]) {
                continue;
            }

            // Filter základen záchranek
            if (!usedEmergencyBases[source.getEmergencyBase().ordinal()]) {
                continue;
            }

            final LocalDateTime fromTime = source.getTime(eventType1);
            final LocalDateTime toTime = source.getTime(eventType2);
            final Duration delta = Duration.between(fromTime, toTime);
            List<Duration> durations = map.computeIfAbsent(source.getEmergencyBase(), k -> new ArrayList<>());
            durations.add(delta);
        }

        final List<Data<String, Long>> averateDataSet = new ArrayList<>();
        final List<Data<String, Long>> minDataSet = new ArrayList<>();
        final List<Data<String, Long>> maxDataSet = new ArrayList<>();

        map.forEach((key, durations) -> {
            final String x = key.cityName;
            if (durations.size() == 1) {
                averateDataSet.add(new Data<>(x, durations.get(0).toMinutes()));
                minDataSet.add(new Data<>(x, durations.get(0).toMinutes()));
                maxDataSet.add(new Data<>(x, durations.get(0).toMinutes()));
                return;
            }

            final Long average = Duration.ofNanos(Math.round(durations.stream().mapToDouble(Duration::toNanos).average().orElse(0.0))).toMinutes();
            final Long min = durations.stream().mapToLong(Duration::toMinutes).min().orElse(15);
            final Long max = durations.stream().mapToLong(Duration::toMinutes).max().orElse(20);

            averateDataSet.add(new Data<>(x, average));
            minDataSet.add(new Data<>(x, min));
            maxDataSet.add(new Data<>(x, max));
        });

        averageSeries.getData().setAll(averateDataSet);
        minSeries.getData().setAll(minDataSet);
        maxSeries.getData().setAll(maxDataSet);
    }

    private void generalButtonHandler(List<Node> cmbList, boolean[] flags, boolean value) {
        cmbList
            .stream()
            .filter(node -> node instanceof CheckBox)
            .map(node -> (CheckBox) node)
            .filter(checkBox -> !checkBox.isSelected())
            .forEach(checkBox -> checkBox.setSelected(value));

        for (int i = 0; i < flags.length; i++) {
            flags[i] = value;
        }

        updateChart();
    }

    private void timeChanged(ObservableValue<? extends EventType> observable, EventType oldValue, EventType newValue) {
        updateChart();
    }

    // endregion

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        initActionCharacters();
        for (int i = 0; i < usedActionCharacters.length; i++) {
            usedActionCharacters[i] = true;
        }
        initBases();
        for (int i = 0; i < usedEmergencyBases.length; i++) {
            usedEmergencyBases[i] = true;
        }

        averageSeries.setName("Průměrný čas");
        minSeries.setName("Minimální čas");
        maxSeries.setName("Maximální čas");

        lineChart.getData().add(minSeries);
        lineChart.getData().add(averageSeries);
        lineChart.getData().add(maxSeries);

        cmbTime1.getItems().setAll(EventType.values());
        cmbTime2.getItems().setAll(EventType.values());

        cmbTime1.getSelectionModel().selectFirst();

        cmbTime1.getSelectionModel().selectedItemProperty().addListener(this::timeChanged);
        cmbTime2.getSelectionModel().selectedItemProperty().addListener(this::timeChanged);

        cmbTime2.getSelectionModel().select(1);
    }

    // region Button handlers

    public void handleActionCharacterAll(ActionEvent actionEvent) {
        generalButtonHandler(vbActionCharacter.getChildren(), usedActionCharacters, true);
    }

    public void handleActionCharacterNone(ActionEvent actionEvent) {
        generalButtonHandler(vbActionCharacter.getChildren(), usedActionCharacters, false);
    }

    public void handleBaseAll(ActionEvent actionEvent) {
        generalButtonHandler(vbBase.getChildren(), usedEmergencyBases, true);
    }

    public void handleBaseNone(ActionEvent actionEvent) {
        generalButtonHandler(vbBase.getChildren(), usedEmergencyBases, true);
    }

    // endregion
}
