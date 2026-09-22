UPDATE company_types
SET foundational_concept_id = (SELECT id FROM concepts WHERE concept_name = 'Cadastro e Login')
WHERE domain_name = 'Banco';

UPDATE company_types
SET foundational_concept_id = (SELECT id FROM concepts WHERE concept_name = 'Cadastro e Login')
WHERE domain_name = 'Startup';

UPDATE company_types
SET foundational_concept_id = (SELECT id FROM concepts WHERE concept_name = 'Cadastro e Login')
WHERE domain_name = 'Prestadora de Serviço';