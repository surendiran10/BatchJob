package com.suren.batchjob.config;

// https://www.youtube.com/watch?v=xak4TWI4Q1I&t=1325s

import com.suren.batchjob.entity.Users;
import com.suren.batchjob.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class BatchConfig {
    @Autowired
    UserRepository userRepository;

    @Bean
    public Job job(JobRepository jobRepository,Step step) {
        return new JobBuilder("import-users", jobRepository)
                .start(step)
                .build();


}

    @Bean
    public Step step(JobRepository jobRepository , PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Users, Users>chunk(100,transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }

    private FlatFileItemReader<Users> reader() {

        return new FlatFileItemReaderBuilder<Users>()
                .name("userItemReader")
                .resource(new ClassPathResource("users.csv"))
                .linesToSkip(1)
                .lineMapper(linemapper())
                .targetType(Users.class).build();
    }

    private LineMapper<Users> linemapper() {
        DefaultLineMapper<Users> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","userId","firstName","lastName","gender","email","phone","dateOfBirth","jobTitle");

        BeanWrapperFieldSetMapper<Users> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Users.class);


        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    UserProcessor processor() {
        return new UserProcessor();

    }

    private RepositoryItemWriter<Users> writer() {

        RepositoryItemWriter<Users> writer = new RepositoryItemWriter<>();
        writer.setRepository(userRepository);
        writer.setMethodName("save");
        return writer;
    }


}
