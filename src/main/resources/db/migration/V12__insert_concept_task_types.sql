INSERT INTO concept_task_types (concept_id, task_type)
SELECT id, 'FEATURE'
FROM concepts
WHERE concept_name = 'Cadastro e Login';

INSERT INTO concept_task_types (concept_id, task_type)
SELECT id, 'FEATURE'
FROM concepts
WHERE concept_name = 'Autenticação JWT';

INSERT INTO concept_task_types (concept_id, task_type)
SELECT id, 'BUGFIX'
FROM concepts
WHERE concept_name = 'Autenticação JWT';

INSERT INTO concept_task_types (concept_id, task_type)
SELECT id, 'FEATURE'
FROM concepts
WHERE concept_name = 'Query method Spring Data';