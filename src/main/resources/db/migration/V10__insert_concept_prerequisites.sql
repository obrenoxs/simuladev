INSERT INTO concept_prerequisites (concept_id, prerequisite_id)
SELECT c1.id, c2.id
FROM concepts c1, concepts c2
WHERE c1.concept_name = 'Autenticação JWT'
    AND c2.concept_name = 'Cadastro e Login';