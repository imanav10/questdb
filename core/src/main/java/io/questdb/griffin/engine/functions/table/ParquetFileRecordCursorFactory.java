/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2024 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.griffin.engine.functions.table;

import io.questdb.cairo.AbstractRecordCursorFactory;
import io.questdb.cairo.sql.RecordCursor;
import io.questdb.cairo.sql.RecordMetadata;
import io.questdb.griffin.SqlExecutionContext;
import io.questdb.std.FilesFacade;
import io.questdb.std.Misc;
import io.questdb.std.Transient;
import io.questdb.std.str.Path;

public class ParquetFileRecordCursorFactory extends AbstractRecordCursorFactory {
    private final ParquetFileRecordCursor cursor;
    private Path path;

    public ParquetFileRecordCursorFactory(@Transient CharSequence path, RecordMetadata metadata, FilesFacade ff) {
        super(metadata);
        this.path = new Path().of(path);
        this.cursor = new ParquetFileRecordCursor(ff, this.path, metadata);
    }

    @Override
    public RecordCursor getCursor(SqlExecutionContext executionContext) {
        cursor.of(executionContext);
        return cursor;
    }

    @Override
    public boolean recordCursorSupportsRandomAccess() {
        return false;
    }

    @Override
    protected void _close() {
        path = Misc.free(path);
    }
}
