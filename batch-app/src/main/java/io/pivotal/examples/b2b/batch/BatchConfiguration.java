package io.pivotal.examples.b2b.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

/**
 * BatchConfiguration
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        LOGGER.info("Instance created");
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<LoanEvent> reader() {
        return new FlatFileItemReaderBuilder<LoanEvent>().name("personItemReader")
                .resource(new ClassPathResource("loans.csv")).delimited()
                .names("loanNumber")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<LoanEvent>() {
                    {
                        setTargetType(LoanEvent.class);
                    }
                }).build();
    }

    @Bean
    public LoanEventProcessor processor() {
        return new LoanEventProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<LoanRate> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<LoanRate>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO loan_rate (loan_number, new_loan_rate) VALUES (:loanNumber, :newLoanRate)").dataSource(dataSource)
                .build();
    }

    @Bean
    public Job enrichLoansJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("updateLoanRatesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<LoanRate> writer) {
        return stepBuilderFactory.get("step1")
                .<LoanEvent, LoanRate>chunk(10000)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .noSkip(FileNotFoundException.class)
                .noRollback(ValidationException.class)
                .build();
    }

}