
This service provides APIs to fetch the features metadata and the images corresponding 
to the feature id provided.

API Specification

    GET /features
    
    Fetches all the features metadata available in the file system

    Returns:
        [
            {
            "id": "39c2f29e-c0f8-4a39-a98b-deed547d6aea",
            "timestamp": 1554831167697,
            "beginViewingDate": 1554831167697,
            "endViewingDate": 1554831202043,
            "missionName": "Sentinel-1"
            },
            {
            "id": "cf5dbe37-ab95-4af1-97ad-2637aec4ddf0",
            "timestamp": 1556904743783,
            "beginViewingDate": 1556904743783,
            "endViewingDate": 1556904768781,
            "missionName": "Sentinel-1"
            }
            ...
        ]

    GET /features/39c2f29e-c0f8-4a39-a98b-deed547d6aea/quicklook

    Returns an image corresponding to the feature id 
    39c2f29e-c0f8-4a39-a98b-deed547d6aea

FeaturesITest test class provides the tests for the above APIs

Further Improvements

Caching mechanism needs to be thought through, when there are large number of features to be stored and retrieved.
Or it could be stored in multi node cluster as and when it comes to the respective nodes.
