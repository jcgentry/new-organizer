package com.jacagen.organizer

import kotlinx.uuid.UUID

actual fun newGuid(): Guid = UUID().toString()